package com.blogapp.blog.dto.response;

import com.blogapp.blog.enums.ReactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionStatusResponse {
    private ReactionType userReaction;
    private long likesCount;
    private long dislikesCount;
}
