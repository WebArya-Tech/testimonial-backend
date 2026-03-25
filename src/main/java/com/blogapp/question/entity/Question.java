package com.blogapp.question.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "questions")
public class Question {
    @Id
    private String id;
    private String title;
    
    @Indexed(unique = true)
    private String slug;

    private String descriptionHtml;
    
    @Indexed
    private String categoryId;
    
    private String adminId; // Who posted it
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
