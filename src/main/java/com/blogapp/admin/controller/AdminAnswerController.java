package com.blogapp.admin.controller;

import com.blogapp.answer.dto.AnswerResponse;
import com.blogapp.answer.enums.AnswerStatus;
import com.blogapp.answer.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/answers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Answer", description = "Admin endpoints for managing user answers")
public class AdminAnswerController {

    private final AnswerService answerService;

    @GetMapping
    @Operation(summary = "List all answers with optional filtering by status/questionId")
    public ResponseEntity<Page<AnswerResponse>> getAllAnswers(
            @RequestParam(required = false) AnswerStatus status,
            @RequestParam(required = false) String questionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(answerService.getAllAnswers(status, questionId, page, size, sort, direction));
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve an answer")
    public ResponseEntity<AnswerResponse> approveAnswer(@PathVariable String id) {
        return ResponseEntity.ok(answerService.updateAnswerStatus(id, AnswerStatus.APPROVED, null));
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject an answer")
    public ResponseEntity<AnswerResponse> rejectAnswer(
            @PathVariable String id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(answerService.updateAnswerStatus(id, AnswerStatus.REJECTED, reason));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an answer")
    public ResponseEntity<Void> deleteAnswer(@PathVariable String id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
