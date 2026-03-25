package com.blogapp.answer.repository;

import com.blogapp.answer.entity.Answer;
import com.blogapp.answer.enums.AnswerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {
    List<Answer> findByQuestionIdAndStatus(String questionId, AnswerStatus status);
    Page<Answer> findByStatus(AnswerStatus status, Pageable pageable);
    Page<Answer> findByQuestionId(String questionId, Pageable pageable);
}
