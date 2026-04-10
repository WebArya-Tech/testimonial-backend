package com.blogapp.contact.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSubjectRequest {
    @NotBlank(message = "Subject name is required")
    @Schema(description = "Name of the topic/subject", example = "Course Inquiry")
    private String name;
}
