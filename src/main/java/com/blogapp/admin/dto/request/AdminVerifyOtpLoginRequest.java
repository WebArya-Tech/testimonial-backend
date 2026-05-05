package com.blogapp.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to verify OTP for Admin login")
public class AdminVerifyOtpLoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(example = "admin@astarclasses.com")
    private String email;

    @NotBlank(message = "OTP code is required")
    private String otp;
}
