package com.blogapp.media.controller;

import com.blogapp.media.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Media Upload", description = "Secure authenticated endpoints for uploading media to Cloudinary")
public class MediaController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @Operation(summary = "Upload media file securely",
            description = "Accepts a file stream, uploads it to Cloudinary utilizing backend credentials, and returns the secure HTTPS URL. Note: User MUST be fully authenticated with a JWT token or this will 403 Forbidden.")
    public ResponseEntity<Map<String, String>> uploadMedia(
            @RequestParam("file") MultipartFile file) {
        
        // This endpoint logic executes safely server-side
        String secureUrl = cloudinaryService.uploadMedia(file);
        
        return ResponseEntity.ok(Map.of(
                "message", "Media uploaded securely",
                "url", secureUrl
        ));
    }
}
