package com.blogapp.answer.service;

import com.blogapp.answer.dto.AnswerRequest;
import com.blogapp.answer.dto.AnswerResponse;
import com.blogapp.answer.enums.AnswerStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnswerService {
    AnswerResponse submitAnswer(AnswerRequest request, String userId);
    List<AnswerResponse> getApprovedAnswersByQuestionId(String questionId);
    
    // Admin methods
    Page<AnswerResponse> getAllAnswers(AnswerStatus status, String questionId, int page, int size, String sort, String direction);
    AnswerResponse updateAnswerStatus(String id, AnswerStatus status, String rejectionReason);
    void deleteAnswer(String id);
}
