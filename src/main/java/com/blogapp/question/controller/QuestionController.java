package com.blogapp.question.controller;

import com.blogapp.question.dto.QuestionResponse;
import com.blogapp.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Tag(name = "Question (Public)", description = "Public endpoints for retrieving questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    @Operation(summary = "Get all questions with optional category filter")
    public ResponseEntity<Page<QuestionResponse>> getQuestions(
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(questionService.getAllQuestions(categoryId, page, size, sort, direction));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a question by ID")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable String id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get a question by slug")
    public ResponseEntity<QuestionResponse> getQuestionBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(questionService.getQuestionBySlug(slug));
    }
}
