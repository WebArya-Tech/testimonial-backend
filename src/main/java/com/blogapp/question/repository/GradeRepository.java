package com.blogapp.question.repository;

import com.blogapp.question.entity.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("questionGradeRepository")
public interface GradeRepository extends MongoRepository<Grade, String> {
}
