package com.blogapp.question.controller;

import com.blogapp.question.dto.QuestionRequest;
import com.blogapp.question.dto.QuestionResponse;
import com.blogapp.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/questions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('USER')")
@Tag(name = "User Question", description = "Endpoints for user question submission and management")
public class UserQuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "Submit a new question as a user")
    public ResponseEntity<QuestionResponse> createQuestion(
            @Valid @RequestBody QuestionRequest request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.blogapp.user.entity.User user) {
        return new ResponseEntity<>(questionService.createQuestion(request, user.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @Operation(summary = "Get questions submitted by the authenticated user")
    public ResponseEntity<Page<QuestionResponse>> getMyQuestions(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.blogapp.user.entity.User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(questionService.getUserQuestions(user.getId(), page, size, sort, direction));
    }
}
