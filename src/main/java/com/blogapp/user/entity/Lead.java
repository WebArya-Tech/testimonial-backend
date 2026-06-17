package com.blogapp.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leads")
public class Lead {
    @Id
    private String id;
    private String name;
    private String mobile;
    private String email;
    private String grade;
    
    private LocalDateTime emailVerifiedAt;

    @CreatedDate
    private LocalDateTime createdAt;
}
