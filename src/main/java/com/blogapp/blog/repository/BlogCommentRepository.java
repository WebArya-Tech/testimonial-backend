package com.blogapp.blog.repository;

import com.blogapp.blog.entity.BlogComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogCommentRepository extends MongoRepository<BlogComment, String> {
    Page<BlogComment> findByBlogIdAndStatus(String blogId, String status, Pageable pageable);
    Page<BlogComment> findByStatus(String status, Pageable pageable);
    long countByBlogIdAndStatus(String blogId, String status);
    long countByIpAddressAndCreatedAtAfter(String ipAddress, java.time.LocalDateTime cutoff);
    void deleteByBlogId(String blogId);
}
