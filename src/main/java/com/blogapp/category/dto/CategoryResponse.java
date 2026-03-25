package com.blogapp.category.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
