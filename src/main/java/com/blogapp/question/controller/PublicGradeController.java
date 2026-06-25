package com.blogapp.question.controller;

import com.blogapp.question.entity.Grade;
import com.blogapp.question.repository.GradeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade (Public)", description = "Public endpoints for grades")
public class PublicGradeController {

    private final GradeRepository gradeRepository;

    public PublicGradeController(@Qualifier("questionGradeRepository") GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @GetMapping
    @Operation(summary = "Get all grades")
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeRepository.findAll(Sort.by(Sort.Direction.ASC, "order")));
    }
}
