package com.blogapp.question.repository;

import com.blogapp.question.entity.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GradeRepository extends MongoRepository<Grade, String> {
}
