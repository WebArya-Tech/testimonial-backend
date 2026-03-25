package com.blogapp.media.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${app.cloudinary.url}") String cloudinaryUrl) {
        // Cloudinary automatically parses the standard cloudinary:// API URL
        this.cloudinary = new Cloudinary(cloudinaryUrl);
        log.info("CloudinaryService initialized securely.");
    }

    /**
     * Generates a secure, time-stamped signature for Direct-to-Cloudinary uploads.
     * This ensures the VPS uses ZERO bandwidth while strictly authorizing user uploads.
     * 
     * @return A map containing the signature and necessary credentials for the frontend API call
     */
    public Map<String, Object> generateSignature() {
        try {
            long timestamp = System.currentTimeMillis() / 1000L;
            Map<String, Object> params = Map.of(
                    "folder", "astar-testimonial-ask",
                    "timestamp", timestamp
            );
            
            String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);
            
            return Map.of(
                    "signature", signature,
                    "timestamp", timestamp,
                    "cloud_name", cloudinary.config.cloudName,
                    "api_key", cloudinary.config.apiKey,
                    "folder", "astar-testimonial-ask"
            );
        } catch (Exception e) {
            log.error("Failed to generate Cloudinary signature", e);
            throw new RuntimeException("Secure Signature Generation Failed: " + e.getMessage());
        }
    }

    /**
     * Deletes a multimedia file natively from Cloudinary's servers.
     * Prevents orphaned payloads from consuming storage quota when testimonials are wiped.
     * 
     * @param secureUrl The full HTTPS URL returned by the upload endpoint
     */
    public void deleteMediaByUrl(String secureUrl) {
        try {
            // URL parse example: https://res.cloudinary.com/demo/image/upload/v123456/astar-testimonial-ask/filename.jpg
            java.net.URL url = java.net.URI.create(secureUrl).toURL();
            String urlPath = url.getPath(); 
            String[] parts = urlPath.split("/upload/");
            
            if (parts.length == 2) {
                String afterUpload = parts[1];
                
                // Strip the version prefix (e.g. "v123456/")
                if (afterUpload.matches("v\\d+/.*")) {
                    afterUpload = afterUpload.replaceFirst("v\\d+/", "");
                }
                
                // Strip the file extension (e.g. ".jpg") to isolate the pristine public_id
                int dotIndex = afterUpload.lastIndexOf('.');
                String publicId = dotIndex != -1 ? afterUpload.substring(0, dotIndex) : afterUpload;
                
                // Natively destroy the file on Cloudinary (auto-detects resource_type as image/video via invalidate)
                cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
                log.info("Successfully wiped orphaned Cloudinary media payload: {}", publicId);
            }
        } catch (Exception e) {
            log.warn("Failed to wipe orphaned Cloudinary media payload at url {}: {}", secureUrl, e.getMessage());
        }
    }
}
