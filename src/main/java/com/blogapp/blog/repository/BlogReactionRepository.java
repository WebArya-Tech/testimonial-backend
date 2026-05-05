package com.blogapp.blog.repository;

import com.blogapp.blog.entity.BlogReaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogReactionRepository extends MongoRepository<BlogReaction, String> {
    Optional<BlogReaction> findByBlogIdAndVisitorKey(String blogId, String visitorKey);
    long countByBlogIdAndType(String blogId, com.blogapp.blog.enums.ReactionType type);
    long countByIpAddressAndCreatedAtAfter(String ipAddress, java.time.LocalDateTime cutoff);
    void deleteByBlogId(String blogId);
}
