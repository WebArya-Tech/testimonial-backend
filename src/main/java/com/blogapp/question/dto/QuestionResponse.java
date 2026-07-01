package com.blogapp.question.dto;

import com.blogapp.question.entity.Grade;
import com.blogapp.question.entity.Subject;
import com.blogapp.question.enums.QuestionStatus;
import com.blogapp.question.enums.QuestionApprovalStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionResponse {
    private String id;
    private String title;
    private String slug;
    private String description;
    private String descriptionHtml;
    private Grade grade;
    private Subject subject;
    private java.util.List<String> attachments;
    private QuestionStatus status;
    private QuestionApprovalStatus approvalStatus;
    private long viewsCount;
    private long answersCount;
    private com.blogapp.common.dto.AuthorDTO author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
