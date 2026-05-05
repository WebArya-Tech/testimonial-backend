package com.blogapp.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to post a new comment on a blog")
public class PostCommentRequest {

    @NotBlank(message = "Name is required")
    @Schema(example = "John Doe")
    private String name;

    @NotBlank(message = "Comment text is required")
    @Schema(example = "Great article!")
    private String commentText;
}
