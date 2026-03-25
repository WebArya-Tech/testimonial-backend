package com.blogapp.question.dto;

import com.blogapp.category.dto.CategoryResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionResponse {
    private String id;
    private String title;
    private String slug;
    private String descriptionHtml;
    private CategoryResponse category;
    private String adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
