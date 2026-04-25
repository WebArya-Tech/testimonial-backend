package com.blogapp.blog.controller;

import com.blogapp.blog.dto.request.SubscriptionOtpRequest;
import com.blogapp.blog.dto.request.SubscriptionRequest;
import com.blogapp.blog.service.BlogSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/blogs/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Public Blog Subscriptions", description = "Endpoints for blog notifications")
public class PublicBlogSubscriptionController {

    private final BlogSubscriptionService subscriptionService;

    @PostMapping("/otp")
    @Operation(summary = "Request subscription OTP", description = "Sends an OTP to verify email ownership before subscribing.")
    public ResponseEntity<Map<String, Object>> requestOtp(@Valid @RequestBody SubscriptionOtpRequest request) {
        subscriptionService.requestOtp(request.getEmail());
        return ResponseEntity.ok(Map.of("success", true, "message", "OTP sent successfully to " + request.getEmail()));
    }

    @PostMapping("/start")
    @Operation(summary = "Subscribe to notifications", description = "Verify OTP and active email subscription for new posts.")
    public ResponseEntity<Map<String, Object>> subscribe(@Valid @RequestBody SubscriptionRequest request) {
        subscriptionService.subscribe(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(Map.of("success", true, "message", "Successfully subscribed to blog notifications."));
    }

    @PostMapping("/stop")
    @Operation(summary = "Unsubscribe", description = "Deactivates blog notifications for the provided email.")
    public ResponseEntity<Map<String, Object>> unsubscribe(@Valid @RequestBody SubscriptionOtpRequest request) {
        subscriptionService.unsubscribe(request.getEmail());
        return ResponseEntity.ok(Map.of("success", true, "message", "Successfully unsubscribed."));
    }
}
