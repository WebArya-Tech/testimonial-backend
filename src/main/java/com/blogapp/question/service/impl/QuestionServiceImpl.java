package com.blogapp.question.service.impl;

import com.blogapp.category.service.CategoryService;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.common.util.HtmlSanitizer;
import com.blogapp.common.util.SlugUtil;
import com.blogapp.question.dto.QuestionRequest;
import com.blogapp.question.dto.QuestionResponse;
import com.blogapp.question.entity.Question;
import com.blogapp.question.repository.QuestionRepository;
import com.blogapp.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryService categoryService;

    @Override
    public QuestionResponse createQuestion(QuestionRequest request, String adminId) {
        String slug = SlugUtil.generateSlug(request.getTitle());
        if (questionRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Question question = Question.builder()
                .title(request.getTitle())
                .slug(slug)
                .descriptionHtml(HtmlSanitizer.sanitize(request.getDescriptionHtml()))
                .categoryId(request.getCategoryId())
                .adminId(adminId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Question saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Override
    public QuestionResponse updateQuestion(String id, QuestionRequest request, String adminId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        question.setTitle(request.getTitle());
        String newSlug = SlugUtil.generateSlug(request.getTitle());
        if (!question.getSlug().equals(newSlug) && questionRepository.existsBySlug(newSlug)) {
            newSlug = newSlug + "-" + System.currentTimeMillis();
        }
        question.setSlug(newSlug);
        question.setDescriptionHtml(HtmlSanitizer.sanitize(request.getDescriptionHtml()));
        question.setCategoryId(request.getCategoryId());
        question.setAdminId(adminId);
        question.setUpdatedAt(LocalDateTime.now());

        Question saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Override
    public void deleteQuestion(String id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found");
        }
        questionRepository.deleteById(id);
    }

    @Override
    public QuestionResponse getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return mapToResponse(question);
    }

    @Override
    public QuestionResponse getQuestionBySlug(String slug) {
        Question question = questionRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return mapToResponse(question);
    }

    @Override
    public Page<QuestionResponse> getAllQuestions(String categoryId, int page, int size, String sort, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        Page<Question> questionsPage;
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            questionsPage = questionRepository.findByCategoryId(categoryId, pageable);
        } else {
            questionsPage = questionRepository.findAll(pageable);
        }

        return questionsPage.map(this::mapToResponse);
    }

    private QuestionResponse mapToResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .slug(question.getSlug())
                .descriptionHtml(question.getDescriptionHtml())
                .category(categoryService.getCategoryById(question.getCategoryId()))
                .adminId(question.getAdminId())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}
