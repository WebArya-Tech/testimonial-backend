package com.blogapp.admin.controller;

import com.blogapp.question.entity.Subject;
import com.blogapp.question.repository.SubjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/subjects")
@RequiredArgsConstructor
@Tag(name = "Admin - Subjects", description = "CRUD operations for Subjects")
public class AdminSubjectController {

    private final SubjectRepository subjectRepository;

    @GetMapping
    @Operation(summary = "Get all subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a subject")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectRepository.save(subject));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a subject")
    public ResponseEntity<Subject> updateSubject(@PathVariable String id, @RequestBody Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id).orElseThrow();
        subject.setName(subjectDetails.getName());
        subject.setGradeId(subjectDetails.getGradeId());
        subject.setOrder(subjectDetails.getOrder());
        return ResponseEntity.ok(subjectRepository.save(subject));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subject")
    public ResponseEntity<Void> deleteSubject(@PathVariable String id) {
        subjectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
