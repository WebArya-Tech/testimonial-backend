package com.blogapp.user.repository;

import com.blogapp.user.entity.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeadRepository extends MongoRepository<Lead, String> {
}
