package com.blogapp.blog.controller;

import com.blogapp.blog.dto.response.BlogSubscriberResponse;
import com.blogapp.blog.service.BlogSubscriptionService;
import com.blogapp.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/blog-subscriptions")
@RequiredArgsConstructor
@Tag(name = "Admin Blog Subscription", description = "Admin APIs for managing blog subscribers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogSubscriptionController {

    private final BlogSubscriptionService blogSubscriptionService;

    @Operation(summary = "Get all blog subscribers", description = "Retrieves a paginated list of all blog subscribers")
    @GetMapping
    public ResponseEntity<PageResponse<BlogSubscriberResponse>> getAllSubscribers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PageResponse<BlogSubscriberResponse> response = blogSubscriptionService.getAllSubscribers(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a blog subscriber", description = "Deletes a subscriber by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable String id) {
        blogSubscriptionService.deleteSubscriber(id);
        return ResponseEntity.noContent().build();
    }
}
