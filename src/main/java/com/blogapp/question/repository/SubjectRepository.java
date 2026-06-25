package com.blogapp.question.repository;

import com.blogapp.question.entity.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

import org.springframework.data.domain.Sort;

public interface SubjectRepository extends MongoRepository<Subject, String> {
    List<Subject> findByGradeId(String gradeId, Sort sort);
}
