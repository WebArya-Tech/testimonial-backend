package com.blogapp.blog.repository;

import com.blogapp.blog.entity.BlogSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogSubscriptionRepository extends MongoRepository<BlogSubscription, String> {

    Optional<BlogSubscription> findByEmail(String email);

    List<BlogSubscription> findByIsActiveTrue();
}
