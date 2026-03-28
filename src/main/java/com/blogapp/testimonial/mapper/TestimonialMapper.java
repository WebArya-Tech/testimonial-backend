package com.blogapp.testimonial.mapper;

import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.entity.Testimonial;

public class TestimonialMapper {

    private TestimonialMapper() {}

    public static TestimonialResponse toResponse(Testimonial testimonial, String teacherName) {
        return TestimonialResponse.builder()
                .id(testimonial.getId())
                .teacherId(testimonial.getTeacherId())
                .teacherName(teacherName)
                .reviewerName(testimonial.getReviewerName())
                .reviewerEmail(testimonial.getReviewerEmail())
                .content(testimonial.getContent())
                .type(testimonial.getType())
                .isPrimary(testimonial.isPrimary())
                .createdAt(testimonial.getCreatedAt())
                .updatedAt(testimonial.getUpdatedAt())
                .build();
    }
}
