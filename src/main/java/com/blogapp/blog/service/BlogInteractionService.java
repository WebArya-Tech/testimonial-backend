package com.blogapp.blog.service;

import com.blogapp.blog.dto.request.PostCommentRequest;
import com.blogapp.blog.dto.request.ToggleReactionRequest;
import com.blogapp.blog.dto.response.ReactionStatusResponse;
import com.blogapp.blog.entity.BlogComment;
import com.blogapp.common.dto.PageResponse;

public interface BlogInteractionService {
    PageResponse<BlogComment> getComments(String blogId, int page, int size);
    void postComment(String blogId, PostCommentRequest request);
    ReactionStatusResponse getReactionStatus(String blogId, String visitorKey);
    ReactionStatusResponse toggleReaction(String blogId, ToggleReactionRequest request);
}
