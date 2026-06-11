package com.blogapp.answer.controller;

import com.blogapp.answer.dto.AnswerRequest;
import com.blogapp.answer.dto.AnswerResponse;
import com.blogapp.answer.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
@Tag(name = "Answer (Public)", description = "Public endpoints for posting and viewing answers")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/question/{questionId}")
    @Operation(summary = "Get all approved answers for a specific question")
    public ResponseEntity<List<AnswerResponse>> getApprovedAnswers(@PathVariable String questionId) {
        return ResponseEntity.ok(answerService.getApprovedAnswersByQuestionId(questionId));
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Submit a new answer (requires authentication, starts as PENDING)")
    public ResponseEntity<AnswerResponse> submitAnswer(
            @Valid @RequestBody AnswerRequest request, 
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.blogapp.user.entity.User user) {
        
        String userId = user.getId();
        return new ResponseEntity<>(answerService.submitAnswer(request, userId), HttpStatus.CREATED);
    }
}
