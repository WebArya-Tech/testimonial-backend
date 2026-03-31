package com.blogapp.testimonial.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTestimonialRequest {

    @NotBlank(message = "Review text is required")
    private String text;

    @NotBlank(message = "Media URL is required")
    private String mediaUrl;
}
