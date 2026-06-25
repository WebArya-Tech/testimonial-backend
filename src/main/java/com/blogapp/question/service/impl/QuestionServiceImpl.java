package com.blogapp.question.service.impl;

import com.blogapp.common.exception.BadRequestException;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.common.util.HtmlSanitizer;
import com.blogapp.common.util.SlugUtil;
import com.blogapp.question.dto.QuestionRequest;
import com.blogapp.question.dto.QuestionResponse;
import com.blogapp.question.entity.Grade;
import com.blogapp.question.entity.Question;
import com.blogapp.question.entity.Subject;
import com.blogapp.question.enums.QuestionApprovalStatus;
import com.blogapp.question.enums.QuestionStatus;
import com.blogapp.question.repository.GradeRepository;
import com.blogapp.question.repository.QuestionRepository;
import com.blogapp.question.repository.SubjectRepository;
import com.blogapp.question.service.QuestionService;
import com.blogapp.user.entity.User;
import com.blogapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Override
    public QuestionResponse createQuestion(QuestionRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isEnrolled()) {
            if (user.getFreeAskOrAnswerCount() >= 3) {
                throw new BadRequestException("Free quota exceeded. Please submit a Lead Form to continue.");
            }
            user.setFreeAskOrAnswerCount(user.getFreeAskOrAnswerCount() + 1);
            userRepository.save(user);
        }

        String slug = SlugUtil.generateSlug(request.getTitle());
        if (questionRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Question question = Question.builder()
                .title(request.getTitle())
                .slug(slug)
                .descriptionHtml(HtmlSanitizer.sanitize(request.getDescriptionHtml()))
                .gradeId(request.getGradeId())
                .subjectId(request.getSubjectId())
                .attachments(request.getAttachments())
                .status(QuestionStatus.OPEN_TO_ANSWER)
                .approvalStatus(QuestionApprovalStatus.PENDING)
                .adminId(userId) // The user who posted it
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Question saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Override
    public QuestionResponse updateQuestion(String id, QuestionRequest request, String adminId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        question.setTitle(request.getTitle());
        String newSlug = SlugUtil.generateSlug(request.getTitle());
        if (!question.getSlug().equals(newSlug) && questionRepository.existsBySlug(newSlug)) {
            newSlug = newSlug + "-" + System.currentTimeMillis();
        }
        question.setSlug(newSlug);
        question.setDescriptionHtml(HtmlSanitizer.sanitize(request.getDescriptionHtml()));
        question.setGradeId(request.getGradeId());
        question.setSubjectId(request.getSubjectId());
        question.setAttachments(request.getAttachments());
        question.setUpdatedAt(LocalDateTime.now());

        Question saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Override
    public void deleteQuestion(String id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found");
        }
        questionRepository.deleteById(id);
    }

    @Override
    public QuestionResponse getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        
        question.setViewsCount(question.getViewsCount() + 1);
        questionRepository.save(question);
        
        return mapToResponse(question);
    }

    @Override
    public QuestionResponse getQuestionBySlug(String slug) {
        Question question = questionRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        
        question.setViewsCount(question.getViewsCount() + 1);
        questionRepository.save(question);
        
        return mapToResponse(question);
    }

    @Override
    public Page<QuestionResponse> getAllQuestions(String keyword, String gradeId, String subjectId, QuestionStatus status, int page, int size, String sort, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        // Public visibility should probably be filtered for APPROVED questions only.
        // We will assume this method is used generally, so let's default to APPROVED if not called by admin?
        // Wait, for public endpoints we just pass QuestionApprovalStatus.APPROVED.
        // I will add approvalStatus to searchQuestions as APPROVED always from the controller for public.
        
        // Wait, we need to handle public vs admin. I will use the custom repository method.
        Page<Question> questionsPage = questionRepository.searchQuestions(keyword, gradeId, subjectId, status, QuestionApprovalStatus.APPROVED, pageable);

        return questionsPage.map(this::mapToResponse);
    }

    @Override
    public Page<QuestionResponse> getAllQuestionsForAdmin(String keyword, String gradeId, String subjectId, QuestionStatus status, QuestionApprovalStatus approvalStatus, int page, int size, String sort, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        
        Page<Question> questionsPage = questionRepository.searchQuestions(keyword, gradeId, subjectId, status, approvalStatus, pageable);
        return questionsPage.map(this::mapToResponse);
    }

    @Override
    public Page<QuestionResponse> getUserQuestions(String userId, int page, int size, String sort, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        
        Page<Question> questionsPage = questionRepository.findByAdminId(userId, pageable);
        return questionsPage.map(this::mapToResponse);
    }

    @Override
    public QuestionResponse updateQuestionStatus(String id, QuestionStatus status) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        question.setStatus(status);
        question.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(questionRepository.save(question));
    }

    @Override
    public QuestionResponse updateQuestionApproval(String id, QuestionApprovalStatus approvalStatus) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        question.setApprovalStatus(approvalStatus);
        question.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(questionRepository.save(question));
    }

    private QuestionResponse mapToResponse(Question question) {
        Grade grade = null;
        if (question.getGradeId() != null) {
            grade = gradeRepository.findById(question.getGradeId()).orElse(null);
        }
        Subject subject = null;
        if (question.getSubjectId() != null) {
            subject = subjectRepository.findById(question.getSubjectId()).orElse(null);
        }

        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .slug(question.getSlug())
                .descriptionHtml(question.getDescriptionHtml())
                .grade(grade)
                .subject(subject)
                .attachments(question.getAttachments())
                .status(question.getStatus())
                .approvalStatus(question.getApprovalStatus())
                .viewsCount(question.getViewsCount())
                .answersCount(question.getAnswersCount())
                .adminId(question.getAdminId())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}
