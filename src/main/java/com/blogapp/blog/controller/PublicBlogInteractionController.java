package com.blogapp.blog.controller;

import com.blogapp.blog.dto.request.PostCommentRequest;
import com.blogapp.blog.dto.request.ToggleReactionRequest;
import com.blogapp.blog.dto.response.ReactionStatusResponse;
import com.blogapp.blog.entity.BlogComment;
import com.blogapp.blog.service.BlogInteractionService;
import com.blogapp.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blogs/{blogId}")
@RequiredArgsConstructor
@Tag(name = "Public Blog Interactions", description = "Endpoints for blog comments and likes/dislikes")
public class PublicBlogInteractionController {

    private final BlogInteractionService interactionService;

    @GetMapping("/comments")
    @Operation(summary = "Get comments", description = "Get paginated visible comments for a blog")
    public ResponseEntity<PageResponse<BlogComment>> getComments(
            @PathVariable String blogId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(interactionService.getComments(blogId, page, size));
    }

    @PostMapping("/comments")
    @Operation(summary = "Post a comment", description = "Post a new comment on a blog")
    public ResponseEntity<Map<String, Object>> postComment(
            @PathVariable String blogId,
            @Valid @RequestBody PostCommentRequest request) {
        interactionService.postComment(blogId, request);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/reactions/status")
    @Operation(summary = "Get reaction status", description = "Get current reaction state for a visitor")
    public ResponseEntity<ReactionStatusResponse> getReactionStatus(
            @PathVariable String blogId,
            @RequestParam(required = false) String visitorKey) {
        return ResponseEntity.ok(interactionService.getReactionStatus(blogId, visitorKey));
    }

    @PostMapping("/reactions/toggle")
    @Operation(summary = "Toggle reaction", description = "Toggle LIKE or DISLIKE for a visitor")
    public ResponseEntity<ReactionStatusResponse> toggleReaction(
            @PathVariable String blogId,
            @Valid @RequestBody ToggleReactionRequest request) {
        return ResponseEntity.ok(interactionService.toggleReaction(blogId, request));
    }
}
