package com.blogapp.testimonial.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
@RequiredArgsConstructor
@Tag(name = "Testimonials", description = "Public endpoints to submit and view testimonials")
public class TestimonialController {

    private final TestimonialService testimonialService;

    @PostMapping
    @Operation(summary = "Submit a testimonial",
            description = "Anyone can submit a review for an existing teacher. "
                    + "Set type=TEXT for plain text reviews, type=URL for Cloudinary media links (video/image/audio). "
                    + "Submissions start as PENDING and require admin approval.")
    public ResponseEntity<TestimonialResponse> submitTestimonial(
            @Valid @RequestBody SubmitTestimonialRequest request) {
        return ResponseEntity.ok(testimonialService.submit(request));
    }

    @GetMapping
    @Operation(summary = "List all approved testimonials")
    public ResponseEntity<List<TestimonialResponse>> getAllApproved() {
        return ResponseEntity.ok(testimonialService.getAllApproved());
    }

    @GetMapping("/primary")
    @Operation(summary = "Get primary/featured testimonials",
            description = "Returns testimonials marked as primary by admin — used for homepage showcase")
    public ResponseEntity<List<TestimonialResponse>> getPrimaryTestimonials() {
        return ResponseEntity.ok(testimonialService.getPrimaryTestimonials());
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get approved testimonials for a specific teacher")
    public ResponseEntity<List<TestimonialResponse>> getByTeacher(
            @Parameter(description = "Teacher ID") @PathVariable String teacherId) {
        return ResponseEntity.ok(testimonialService.getApprovedByTeacher(teacherId));
    }
}
