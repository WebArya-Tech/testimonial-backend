package com.blogapp.testimonial.mapper;

import com.blogapp.testimonial.dto.response.TestimonialResponse;
import com.blogapp.testimonial.entity.Testimonial;

public class TestimonialMapper {

    private TestimonialMapper() {}

    public static TestimonialResponse toResponse(Testimonial testimonial) {
        return TestimonialResponse.builder()
                .id(testimonial.getId())
                .text(testimonial.getText())
                .mediaUrl(testimonial.getMediaUrl())
                .isPrimary(testimonial.isPrimary())
                .createdAt(testimonial.getCreatedAt())
                .updatedAt(testimonial.getUpdatedAt())
                .build();
    }
}
