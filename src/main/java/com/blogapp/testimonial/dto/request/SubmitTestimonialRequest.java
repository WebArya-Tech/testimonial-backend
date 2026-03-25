package com.blogapp.testimonial.dto.request;

import com.blogapp.testimonial.enums.TestimonialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTestimonialRequest {

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    @NotBlank(message = "Reviewer name is required")
    private String reviewerName;

    private String reviewerEmail;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Type is required (TEXT or URL)")
    private TestimonialType type;
}
