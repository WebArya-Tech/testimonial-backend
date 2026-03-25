package com.blogapp.testimonial.dto.response;

import com.blogapp.testimonial.enums.TestimonialStatus;
import com.blogapp.testimonial.enums.TestimonialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestimonialResponse {

    private String id;
    private String teacherId;
    private String teacherName;
    private String reviewerName;
    private String reviewerEmail;
    private String content;
    private TestimonialType type;
    private TestimonialStatus status;
    private boolean isPrimary;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
