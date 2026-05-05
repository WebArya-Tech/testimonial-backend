package com.blogapp.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Optional request body for approving a blog post")
public class ApproveBlogRequest {

    @Schema(description = "ID of the admin making the approval. Defaults to system admin if not provided.", example = "admin_user_123")
    private String adminId;
}
