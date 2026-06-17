package com.blogapp.question.repository;

import com.blogapp.question.entity.Question;
import com.blogapp.question.enums.QuestionStatus;
import com.blogapp.question.enums.QuestionApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Question> searchQuestions(String keyword, String gradeId, String subjectId, QuestionStatus status, QuestionApprovalStatus approvalStatus, Pageable pageable) {
        Query query = new Query();

        if (keyword != null && !keyword.trim().isEmpty()) {
            Criteria keywordCriteria = new Criteria().orOperator(
                    Criteria.where("title").regex(keyword, "i"),
                    Criteria.where("descriptionHtml").regex(keyword, "i")
            );
            query.addCriteria(keywordCriteria);
        }

        if (gradeId != null && !gradeId.trim().isEmpty()) {
            query.addCriteria(Criteria.where("gradeId").is(gradeId));
        }

        if (subjectId != null && !subjectId.trim().isEmpty()) {
            query.addCriteria(Criteria.where("subjectId").is(subjectId));
        }

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        if (approvalStatus != null) {
            query.addCriteria(Criteria.where("approvalStatus").is(approvalStatus));
        }

        long count = mongoTemplate.count(query, Question.class);
        
        query.with(pageable);
        List<Question> questions = mongoTemplate.find(query, Question.class);

        return new PageImpl<>(questions, pageable, count);
    }
}
