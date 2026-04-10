package com.blogapp.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponse {
    private String id;
    private String name;
    private LocalDateTime createdAt;
}
