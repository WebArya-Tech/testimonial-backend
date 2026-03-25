package com.blogapp.answer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerRequest {
    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "Answer content cannot be empty")
    private String contentHtml;
}
