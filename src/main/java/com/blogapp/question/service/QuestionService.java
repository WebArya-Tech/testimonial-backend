package com.blogapp.question.service;

import com.blogapp.question.dto.QuestionRequest;
import com.blogapp.question.dto.QuestionResponse;
import org.springframework.data.domain.Page;

public interface QuestionService {
    QuestionResponse createQuestion(QuestionRequest request, String adminId);
    QuestionResponse updateQuestion(String id, QuestionRequest request, String adminId);
    void deleteQuestion(String id);
    QuestionResponse getQuestionById(String id);
    QuestionResponse getQuestionBySlug(String slug);
    Page<QuestionResponse> getAllQuestions(String categoryId, int page, int size, String sort, String direction);
}
