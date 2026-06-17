package com.blogapp.user.controller;

import com.blogapp.otp.enums.OtpPurpose;
import com.blogapp.otp.service.OtpService;
import com.blogapp.user.entity.Lead;
import com.blogapp.user.repository.LeadRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@Tag(name = "Lead Generation", description = "Endpoints for visitors who exhaust free Ask limit to submit Lead form")
public class LeadController {

    private final OtpService otpService;
    private final LeadRepository leadRepository;

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP for Lead verification")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody LeadOtpRequest request) {
        log.info("Sending lead verification OTP to: {}", request.getEmail());
        boolean sent = otpService.sendOtp(request.getEmail(), OtpPurpose.LEAD_VERIFICATION, request.isResend());
        if (!sent) {
            return ResponseEntity.ok(Map.of("message", "OTP recently sent, please check your email."));
        }
        return ResponseEntity.ok(Map.of("message", "OTP sent to " + request.getEmail()));
    }

    @PostMapping("/submit")
    @Operation(summary = "Verify OTP and submit Lead Form")
    public ResponseEntity<Map<String, String>> submitLeadForm(@Valid @RequestBody LeadSubmitRequest request) {
        log.info("Verifying lead OTP for: {}", request.getEmail());
        boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp(), OtpPurpose.LEAD_VERIFICATION);
        if (!valid) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));
        }

        Lead lead = Lead.builder()
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .grade(request.getGrade())
                .emailVerifiedAt(LocalDateTime.now())
                .build();
        leadRepository.save(lead);
        log.info("Lead saved successfully for: {}", request.getEmail());

        return ResponseEntity.ok(Map.of("message", "Lead submitted and verified successfully"));
    }

    @Data
    public static class LeadOtpRequest {
        @NotBlank @Email
        private String email;
        private boolean resend;
    }

    @Data
    public static class LeadSubmitRequest {
        @NotBlank @Email
        private String email;
        @NotBlank
        private String otp;
        @NotBlank
        private String name;
        @NotBlank
        private String mobile;
        @NotBlank
        private String grade;
    }
}
