package com.blogapp.admin.controller;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.testimonial.dto.request.SubmitTestimonialRequest;
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
    @Operation(summary = "List all testimonials globally", description = "Admin dashboard list of all live testimonials")
    public ResponseEntity<PageResponse<TestimonialResponse>> getAllTestimonials(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(testimonialService.getPaginated(page, size));
    }

    @PostMapping
    @Operation(summary = "Admin creates a Testimonial", description = "Since public submissions are closed, Admins manually submit sourced testimonials here")
    public ResponseEntity<TestimonialResponse> createTestimonial(
            @Valid @RequestBody SubmitTestimonialRequest request) {
        return ResponseEntity.ok(testimonialService.submit(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Admin edits an existing Testimonial")
    public ResponseEntity<TestimonialResponse> updateTestimonial(
            @Parameter(description = "Testimonial ID") @PathVariable String id,
            @Valid @RequestBody SubmitTestimonialRequest request) {
        return ResponseEntity.ok(testimonialService.update(id, request));
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
