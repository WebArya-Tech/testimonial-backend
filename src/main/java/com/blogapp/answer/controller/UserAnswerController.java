package com.blogapp.answer.controller;

import com.blogapp.answer.dto.AnswerResponse;
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
@RequestMapping("/api/user/answers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('USER')")
@Tag(name = "User Answer", description = "Endpoints for user answer management")
public class UserAnswerController {

    private final AnswerService answerService;

    @GetMapping("/me")
    @Operation(summary = "Get answers submitted by the authenticated user")
    public ResponseEntity<Page<AnswerResponse>> getMyAnswers(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.blogapp.user.entity.User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(answerService.getUserAnswers(user.getId(), page, size, sort, direction));
    }
}
