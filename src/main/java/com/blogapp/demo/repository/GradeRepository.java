package com.blogapp.demo.repository;

import com.blogapp.demo.entity.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends MongoRepository<Grade, String> {
}
