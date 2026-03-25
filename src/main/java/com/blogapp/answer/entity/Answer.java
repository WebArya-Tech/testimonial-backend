package com.blogapp.answer.entity;

import com.blogapp.answer.enums.AnswerStatus;
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
@Document(collection = "answers")
public class Answer {
    @Id
    private String id;

    @Indexed
    private String questionId;

    @Indexed
    private String userId;

    private String authorName;

    private String contentHtml;

    @Indexed
    private AnswerStatus status;

    private String rejectionReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
