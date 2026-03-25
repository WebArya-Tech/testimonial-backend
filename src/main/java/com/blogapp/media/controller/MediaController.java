package com.blogapp.media.controller;

import com.blogapp.media.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Media Upload", description = "Secure Direct-to-Cloudinary (Signed Upload) endpoints")
public class MediaController {

    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    @Operation(summary = "Get secure Cloudinary upload signature",
            description = "Returns a cryptographically signed ticket allowing the frontend to upload a massive video/image directly to Cloudinary without consuming server bandwidth. Requires a valid JWT User/Admin token.")
    public ResponseEntity<Map<String, Object>> getSignature() {
        
        Map<String, Object> signatureData = cloudinaryService.generateSignature();
        return ResponseEntity.ok(signatureData);
        
    }
}
