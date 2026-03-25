package com.blogapp.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admins")
public class Admin {
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password; // bcrypt hashed

    @CreatedDate
    private LocalDateTime createdAt;
}
