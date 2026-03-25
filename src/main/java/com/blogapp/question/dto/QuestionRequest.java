package com.blogapp.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String descriptionHtml;

    @NotBlank(message = "Category ID is required")
    private String categoryId;
}
