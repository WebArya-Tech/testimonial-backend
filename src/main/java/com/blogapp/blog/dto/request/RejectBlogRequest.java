package com.blogapp.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for rejecting a blog post")
public class RejectBlogRequest {

    @NotBlank(message = "Reason for rejection is required")
    @Schema(description = "Reason why the blog post was rejected", example = "The content does not meet our styling guidelines.")
    private String reason;
}
