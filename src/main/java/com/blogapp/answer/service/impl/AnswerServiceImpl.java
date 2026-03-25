package com.blogapp.answer.service.impl;

import com.blogapp.answer.dto.AnswerRequest;
import com.blogapp.answer.dto.AnswerResponse;
import com.blogapp.answer.entity.Answer;
import com.blogapp.answer.enums.AnswerStatus;
import com.blogapp.answer.repository.AnswerRepository;
import com.blogapp.answer.service.AnswerService;
import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.common.util.HtmlSanitizer;
import com.blogapp.question.repository.QuestionRepository;
import com.blogapp.user.entity.User;
import com.blogapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    public AnswerResponse submitAnswer(AnswerRequest request, String userId) {
        if (!questionRepository.existsById(request.getQuestionId())) {
            throw new ResourceNotFoundException("Question not found");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Answer answer = Answer.builder()
                .questionId(request.getQuestionId())
                .userId(user.getId())
                .authorName(user.getName())
                .contentHtml(HtmlSanitizer.sanitize(request.getContentHtml()))
                .status(AnswerStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Answer saved = answerRepository.save(answer);
        return mapToResponse(saved);
    }

    @Override
    public List<AnswerResponse> getApprovedAnswersByQuestionId(String questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found");
        }
        
        return answerRepository.findByQuestionIdAndStatus(questionId, AnswerStatus.APPROVED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AnswerResponse> getAllAnswers(AnswerStatus status, String questionId, int page, int size, String sort, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        Page<Answer> answersPage;
        if (questionId != null && !questionId.trim().isEmpty()) {
            answersPage = answerRepository.findByQuestionId(questionId, pageable);
        } else if (status != null) {
            answersPage = answerRepository.findByStatus(status, pageable);
        } else {
            answersPage = answerRepository.findAll(pageable);
        }

        return answersPage.map(this::mapToResponse);
    }

    @Override
    public AnswerResponse updateAnswerStatus(String id, AnswerStatus status, String rejectionReason) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        answer.setStatus(status);
        if (status == AnswerStatus.REJECTED) {
            answer.setRejectionReason(rejectionReason);
        } else {
            answer.setRejectionReason(null);
        }
        answer.setUpdatedAt(LocalDateTime.now());

        Answer saved = answerRepository.save(answer);
        return mapToResponse(saved);
    }

    @Override
    public void deleteAnswer(String id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Answer not found");
        }
        answerRepository.deleteById(id);
    }

    private AnswerResponse mapToResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .userId(answer.getUserId())
                .authorName(answer.getAuthorName())
                .contentHtml(answer.getContentHtml())
                .status(answer.getStatus())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
}
