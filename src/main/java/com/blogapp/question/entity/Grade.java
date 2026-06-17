package com.blogapp.question.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "grades")
public class Grade {
    @Id
    private String id;
    private String name; // e.g., "Primary (Grades 1-5)"
    private int order; // for sorting dropdowns
}
