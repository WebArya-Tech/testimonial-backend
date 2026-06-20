package com.blogapp.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String descriptionHtml;

    @NotBlank(message = "Grade ID is required")
    private String gradeId;

    @NotBlank(message = "Subject ID is required")
    private String subjectId;

    private java.util.List<String> attachments;
}
