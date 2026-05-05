package com.blogapp.blog.controller;

import com.blogapp.blog.entity.BlogComment;
import com.blogapp.blog.service.BlogInteractionService;
import com.blogapp.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/comments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Comment Moderation", description = "Admin endpoints for reviewing blog comments")
public class AdminCommentController {

    private final BlogInteractionService interactionService;

    @GetMapping("/pending")
    @Operation(summary = "Get pending comments", description = "List all comments waiting for admin approval")
    public ResponseEntity<PageResponse<BlogComment>> getPendingComments(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(interactionService.getPendingComments(page, size));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all comments", description = "List all comments regardless of status")
    public ResponseEntity<PageResponse<BlogComment>> getAllComments(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(interactionService.getAllComments(page, size));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve comment", description = "Approves a pending comment, making it visible to the public")
    public ResponseEntity<BlogComment> approveComment(@PathVariable String id) {
        return ResponseEntity.ok(interactionService.approveComment(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit comment", description = "Edits the text content of a comment")
    public ResponseEntity<BlogComment> editComment(
            @PathVariable String id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(interactionService.editComment(id, body.get("commentText")));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment", description = "Deletes any comment completely")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        interactionService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
