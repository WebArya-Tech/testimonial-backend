package com.blogapp.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleDemoRequest {
    @NotBlank(message = "Student name is required")
    @Schema(description = "Full name of the student", example = "John Doe")
    private String studentName;

    @NotBlank(message = "Parent name is required")
    @Schema(description = "Full name of the parent", example = "Jane Doe")
    private String parentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Contact email address", example = "student@email.com")
    private String emailId;

    @NotBlank(message = "Mobile number is required")
    @Schema(description = "Contact mobile number", example = "+91-8861919000")
    private String mobileNumber;

    @NotBlank(message = "Board ID is required")
    @Schema(description = "ID of the selected board", example = "60f1b2b3b3b3b3b3b3b3b3b3")
    private String boardId;

    @NotBlank(message = "Grade ID is required")
    @Schema(description = "ID of the selected grade", example = "60f1b2b3b3b3b3b3b3b3b3b4")
    private String gradeId;

    @NotNull(message = "Preferred date is required")
    @Schema(description = "Preferred date for the demo in YYYY-MM-DD format", example = "2026-04-15")
    private LocalDate preferredDate;

    @NotBlank(message = "Preferred time is required")
    @Schema(description = "Preferred time for the demo", example = "11:00 AM")
    private String preferredTime;

    @NotBlank(message = "OTP is required")
    @Schema(description = "OTP sent to the email address", example = "123456")
    private String otp;
}
