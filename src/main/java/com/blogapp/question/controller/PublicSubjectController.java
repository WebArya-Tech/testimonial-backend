package com.blogapp.question.controller;

import com.blogapp.question.entity.Subject;
import com.blogapp.question.repository.SubjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Subject (Public)", description = "Public endpoints for subjects")
public class PublicSubjectController {

    private final SubjectRepository subjectRepository;

    @GetMapping
    @Operation(summary = "Get all subjects, optionally filtered by gradeId")
    public ResponseEntity<List<Subject>> getSubjects(@RequestParam(required = false) String gradeId) {
        List<Subject> subjects;
        Sort sort = Sort.by(Sort.Direction.ASC, "order");
        if (gradeId != null && !gradeId.trim().isEmpty()) {
            subjects = subjectRepository.findByGradeId(gradeId, sort);
        } else {
            subjects = subjectRepository.findAll(sort);
        }
        return ResponseEntity.ok(subjects);
    }
}
