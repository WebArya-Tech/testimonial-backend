package com.blogapp.testimonial.controller;

import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.service.TestimonialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping
    @Operation(summary = "List all live testimonials")
    public ResponseEntity<List<TestimonialResponse>> getAll() {
        return ResponseEntity.ok(testimonialService.getAll());
    }

    @GetMapping("/primary")
    @Operation(summary = "Get primary/featured testimonials",
            description = "Returns testimonials marked as primary by admin — used for homepage showcase")
    public ResponseEntity<List<TestimonialResponse>> getPrimaryTestimonials() {
        return ResponseEntity.ok(testimonialService.getPrimaryTestimonials());
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get testimonials for a specific teacher")
    public ResponseEntity<List<TestimonialResponse>> getByTeacher(
            @Parameter(description = "Teacher ID") @PathVariable String teacherId) {
        return ResponseEntity.ok(testimonialService.getByTeacher(teacherId));
    }
}
