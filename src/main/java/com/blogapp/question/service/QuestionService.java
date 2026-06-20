package com.blogapp.question.service;

import com.blogapp.question.dto.QuestionRequest;
import com.blogapp.question.dto.QuestionResponse;
import org.springframework.data.domain.Page;

public interface QuestionService {
    QuestionResponse createQuestion(QuestionRequest request, String userId);
    QuestionResponse updateQuestion(String id, QuestionRequest request, String adminId);
    void deleteQuestion(String id);
    QuestionResponse getQuestionById(String id);
    QuestionResponse getQuestionBySlug(String slug);
    Page<QuestionResponse> getAllQuestions(String keyword, String gradeId, String subjectId, com.blogapp.question.enums.QuestionStatus status, int page, int size, String sort, String direction);
    
    QuestionResponse updateQuestionStatus(String id, com.blogapp.question.enums.QuestionStatus status);
    QuestionResponse updateQuestionApproval(String id, com.blogapp.question.enums.QuestionApprovalStatus approvalStatus);
}
