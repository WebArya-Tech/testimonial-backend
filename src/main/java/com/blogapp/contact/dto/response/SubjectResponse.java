package com.blogapp.contact.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubjectResponse {
    private String id;
    private String name;
    private LocalDateTime createdAt;
}
