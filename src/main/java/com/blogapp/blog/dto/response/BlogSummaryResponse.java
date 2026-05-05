package com.blogapp.blog.dto.response;

import com.blogapp.blog.enums.BlogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Summary view of a blog post (for listing pages)")
public class BlogSummaryResponse {

    @Schema(description = "Blog ID", example = "60f1b2b3b3b3b3b3b3b3b3b3")
    private String id;

    @Schema(description = "Blog title", example = "How to Prepare for IGCSE Physics")
    private String title;

    @Schema(description = "URL-friendly slug", example = "how-to-prepare-for-igcse-physics")
    private String slug;

    @Schema(description = "Short excerpt", example = "A comprehensive guide to acing IGCSE Physics exams")
    private String excerpt;

    @Schema(description = "Featured image URL", example = "https://example.com/images/physics.jpg")
    private String featuredImageUrl;

    @Schema(description = "Author name", example = "Jane Doe")
    private String authorName;

    @Schema(description = "Blog status", example = "PUBLISHED")
    private BlogStatus status;

    @Schema(description = "Published date")
    private LocalDateTime publishedAt;

    @Schema(description = "Tags", example = "[\"physics\", \"igcse\"]")
    private List<String> tags;

    @Schema(description = "Total likes", example = "42")
    private long likesCount;

    @Schema(description = "Total dislikes", example = "2")
    private long dislikesCount;

    @Schema(description = "Total comments", example = "5")
    private long commentsCount;

    @Schema(description = "Total views", example = "1050")
    private long viewsCount;
}
