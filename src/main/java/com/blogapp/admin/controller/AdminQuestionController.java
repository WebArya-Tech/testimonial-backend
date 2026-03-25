package com.blogapp.admin.controller;

import com.blogapp.question.dto.QuestionRequest;
import com.blogapp.question.dto.QuestionResponse;
import com.blogapp.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Question", description = "Admin endpoints for managing questions")
public class AdminQuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "Create a new question")
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request, Principal principal) {
        String adminId = principal != null ? principal.getName() : "admin";
        return new ResponseEntity<>(questionService.createQuestion(request, adminId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing question")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable String id, 
            @Valid @RequestBody QuestionRequest request,
            Principal principal) {
        String adminId = principal != null ? principal.getName() : "admin";
        return ResponseEntity.ok(questionService.updateQuestion(id, request, adminId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a question")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
