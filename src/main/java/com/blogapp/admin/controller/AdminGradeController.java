package com.blogapp.admin.controller;

import com.blogapp.question.entity.Grade;
import com.blogapp.question.repository.GradeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/grades")
@RequiredArgsConstructor
@Tag(name = "Admin - Grades", description = "CRUD operations for Grades")
public class AdminGradeController {

    private final GradeRepository gradeRepository;

    @GetMapping
    @Operation(summary = "Get all grades")
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a grade")
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        return ResponseEntity.ok(gradeRepository.save(grade));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a grade")
    public ResponseEntity<Grade> updateGrade(@PathVariable String id, @RequestBody Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id).orElseThrow();
        grade.setName(gradeDetails.getName());
        grade.setOrder(gradeDetails.getOrder());
        return ResponseEntity.ok(gradeRepository.save(grade));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grade")
    public ResponseEntity<Void> deleteGrade(@PathVariable String id) {
        gradeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
