package com.blogapp.answer.dto;

import com.blogapp.answer.enums.AnswerStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnswerResponse {
    private String id;
    private String questionId;
    private String userId;
    private String authorName;
    private String contentHtml;
    private AnswerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
