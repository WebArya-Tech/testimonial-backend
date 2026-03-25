package com.blogapp.media.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * Uploads a multimedia file to Cloudinary.
     * Supports images, videos, and raw files natively.
     * 
     * @param file The file stream from the frontend
     * @return The secure HTTPS URL of the uploaded asset
     */
    public String uploadMedia(MultipartFile file) {
        try {
            // "auto" resource_type allows Cloudinary to automatically detect if it's an image or video
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "astar-testimonial-ask"
            ));
            
            return uploadResult.get("secure_url").toString();
            
        } catch (IOException e) {
            log.error("Failed to upload media to Cloudinary", e);
            throw new RuntimeException("Media upload failed securely: " + e.getMessage());
        }
    }
}
