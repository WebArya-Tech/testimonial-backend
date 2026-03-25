package com.blogapp.admin.controller;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.testimonial.dto.request.RejectTestimonialRequest;
import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.service.TestimonialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/testimonials")
@RequiredArgsConstructor
@Tag(name = "Admin - Testimonials", description = "Admin endpoints to moderate testimonials")
public class AdminTestimonialController {

    private final TestimonialService testimonialService;

    @GetMapping
    @Operation(summary = "List all testimonials",
            description = "List testimonials with optional status filter (PENDING, APPROVED, REJECTED)")
    public ResponseEntity<PageResponse<TestimonialResponse>> getAllTestimonials(
            @Parameter(description = "Filter by status: PENDING, APPROVED, REJECTED") @RequestParam(required = false) String status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(testimonialService.getAll(status, page, size));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a testimonial")
    public ResponseEntity<TestimonialResponse> approve(
            @Parameter(description = "Testimonial ID") @PathVariable String id) {
        return ResponseEntity.ok(testimonialService.approve(id, "admin"));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a testimonial", description = "Rejection reason is required")
    public ResponseEntity<TestimonialResponse> reject(
            @Parameter(description = "Testimonial ID") @PathVariable String id,
            @Valid @RequestBody RejectTestimonialRequest request) {
        return ResponseEntity.ok(testimonialService.reject(id, request.getReason()));
    }

    @PostMapping("/{id}/primary")
    @Operation(summary = "Set testimonial as primary",
            description = "Marks this testimonial as the primary/featured one for its teacher. "
                    + "Only one primary per teacher — any existing primary is cleared.")
    public ResponseEntity<TestimonialResponse> setPrimary(
            @Parameter(description = "Testimonial ID") @PathVariable String id) {
        return ResponseEntity.ok(testimonialService.setPrimary(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a testimonial")
    public ResponseEntity<Map<String, String>> delete(
            @Parameter(description = "Testimonial ID") @PathVariable String id) {
        testimonialService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Testimonial deleted successfully"));
    }
}
