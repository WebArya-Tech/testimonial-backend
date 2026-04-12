package com.blogapp.blog.entity;

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
@Document(collection = "blog_comments")
public class BlogComment {

    @Id
    private String id;

    @Indexed
    private String blogId;

    private String name;

    private String commentText;

    @Builder.Default
    private String status = "VISIBLE";

    @CreatedDate
    private LocalDateTime createdAt;
}
