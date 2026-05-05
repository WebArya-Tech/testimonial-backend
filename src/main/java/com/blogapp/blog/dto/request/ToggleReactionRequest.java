package com.blogapp.blog.dto.request;

import com.blogapp.blog.enums.ReactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to toggle a LIKE or DISLIKE reaction on a blog")
public class ToggleReactionRequest {

    @NotBlank(message = "Visitor key is required")
    @Schema(description = "Unique anonymous identifier from frontend local storage")
    private String visitorKey;

    @NotNull(message = "Reaction type is required")
    private ReactionType reactionType;
}
