package com.blogapp.contact.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactMessageRequest {
    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the sender", example = "John Doe")
    private String fullName;

    @Schema(description = "Optional phone number", example = "+91-8861919000")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Contact email address", example = "your.email@example.com")
    private String emailAddress;

    @NotBlank(message = "Subject ID is required")
    @Schema(description = "ID of the selected subject", example = "60f1b2b3b3b3b3b3b3b3b3b5")
    private String subjectId;

    @NotBlank(message = "Message text is required")
    @Schema(description = "The message body from the user", example = "I would like to know more about the IGCSE physics course.")
    private String messageText;
}
