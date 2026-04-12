package com.blogapp.blog.service.impl;

import com.blogapp.blog.dto.request.PostCommentRequest;
import com.blogapp.blog.dto.request.ToggleReactionRequest;
import com.blogapp.blog.dto.response.ReactionStatusResponse;
import com.blogapp.blog.entity.BlogComment;
import com.blogapp.blog.entity.BlogPost;
import com.blogapp.blog.entity.BlogReaction;
import com.blogapp.blog.enums.ReactionType;
import com.blogapp.blog.repository.BlogCommentRepository;
import com.blogapp.blog.repository.BlogPostRepository;
import com.blogapp.blog.repository.BlogReactionRepository;
import com.blogapp.blog.service.BlogInteractionService;
import com.blogapp.common.dto.PageResponse;
import com.blogapp.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogInteractionServiceImpl implements BlogInteractionService {

    private final BlogCommentRepository blogCommentRepository;
    private final BlogReactionRepository blogReactionRepository;
    private final BlogPostRepository blogPostRepository;

    private BlogPost getBlog(String blogId) {
        return blogPostRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", blogId));
    }

    @Override
    public PageResponse<BlogComment> getComments(String blogId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BlogComment> commentPage = blogCommentRepository.findByBlogIdAndStatus(blogId, "VISIBLE", pageable);

        return PageResponse.<BlogComment>builder()
                .content(commentPage.getContent())
                .page(commentPage.getNumber())
                .size(commentPage.getSize())
                .totalElements(commentPage.getTotalElements())
                .totalPages(commentPage.getTotalPages())
                .first(commentPage.isFirst())
                .last(commentPage.isLast())
                .build();
    }

    @Override
    public void postComment(String blogId, PostCommentRequest request) {
        BlogPost blog = getBlog(blogId);

        BlogComment comment = BlogComment.builder()
                .blogId(blogId)
                .name(request.getName())
                .commentText(request.getCommentText())
                .status("VISIBLE") // Automatically visible payload according to current scope
                .build();

        blogCommentRepository.save(comment);

        blog.setCommentsCount(blog.getCommentsCount() + 1);
        blogPostRepository.save(blog);
    }

    @Override
    public ReactionStatusResponse getReactionStatus(String blogId, String visitorKey) {
        BlogPost blog = getBlog(blogId);

        ReactionType userReaction = null;
        if (visitorKey != null && !visitorKey.isEmpty() && !visitorKey.equals("anonymous")) {
            Optional<BlogReaction> existing = blogReactionRepository.findByBlogIdAndVisitorKey(blogId, visitorKey);
            if (existing.isPresent()) {
                userReaction = existing.get().getType();
            }
        }

        return ReactionStatusResponse.builder()
                .userReaction(userReaction)
                .likesCount(blog.getLikesCount())
                .dislikesCount(blog.getDislikesCount())
                .build();
    }

    @Override
    public ReactionStatusResponse toggleReaction(String blogId, ToggleReactionRequest request) {
        BlogPost blog = getBlog(blogId);
        String visitorKey = request.getVisitorKey();
        ReactionType requestedType = request.getReactionType();

        Optional<BlogReaction> existingOpt = blogReactionRepository.findByBlogIdAndVisitorKey(blogId, visitorKey);

        ReactionType finalReaction = null;

        if (existingOpt.isPresent()) {
            BlogReaction existingReaction = existingOpt.get();
            if (existingReaction.getType() == requestedType) {
                // Remove reaction
                blogReactionRepository.delete(existingReaction);
                if (requestedType == ReactionType.LIKE) blog.setLikesCount(Math.max(0, blog.getLikesCount() - 1));
                if (requestedType == ReactionType.DISLIKE) blog.setDislikesCount(Math.max(0, blog.getDislikesCount() - 1));
            } else {
                // Switch reaction
                if (existingReaction.getType() == ReactionType.LIKE) {
                    blog.setLikesCount(Math.max(0, blog.getLikesCount() - 1));
                    blog.setDislikesCount(blog.getDislikesCount() + 1);
                } else {
                    blog.setDislikesCount(Math.max(0, blog.getDislikesCount() - 1));
                    blog.setLikesCount(blog.getLikesCount() + 1);
                }
                existingReaction.setType(requestedType);
                blogReactionRepository.save(existingReaction);
                finalReaction = requestedType;
            }
        } else {
            // New reaction
            BlogReaction newReaction = BlogReaction.builder()
                    .blogId(blogId)
                    .visitorKey(visitorKey)
                    .type(requestedType)
                    .build();
            blogReactionRepository.save(newReaction);
            
            if (requestedType == ReactionType.LIKE) blog.setLikesCount(blog.getLikesCount() + 1);
            if (requestedType == ReactionType.DISLIKE) blog.setDislikesCount(blog.getDislikesCount() + 1);
            finalReaction = requestedType;
        }

        blogPostRepository.save(blog);

        return ReactionStatusResponse.builder()
                .userReaction(finalReaction)
                .likesCount(blog.getLikesCount())
                .dislikesCount(blog.getDislikesCount())
                .build();
    }
}
