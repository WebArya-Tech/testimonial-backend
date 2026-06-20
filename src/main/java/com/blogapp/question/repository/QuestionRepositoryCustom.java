package com.blogapp.question.repository;

import com.blogapp.question.dto.QuestionResponse;
import com.blogapp.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {
    Page<Question> searchQuestions(String keyword, String gradeId, String subjectId, com.blogapp.question.enums.QuestionStatus status, com.blogapp.question.enums.QuestionApprovalStatus approvalStatus, Pageable pageable);
}
