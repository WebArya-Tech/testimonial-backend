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
    private com.blogapp.common.dto.AuthorDTO author;
    private String description;
    private String contentHtml;
    private AnswerStatus status;
    private java.util.List<String> attachments;
    private boolean isCorrect;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
