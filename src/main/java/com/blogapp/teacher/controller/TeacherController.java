package com.blogapp.teacher.controller;

import com.blogapp.teacher.dto.response.TeacherResponse;
import com.blogapp.teacher.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Public endpoints to list active teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "List all active teachers")
    public ResponseEntity<List<TeacherResponse>> getAllActiveTeachers() {
        return ResponseEntity.ok(teacherService.getAllActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<TeacherResponse> getTeacherById(
            @Parameter(description = "Teacher ID") @PathVariable String id) {
        return ResponseEntity.ok(teacherService.getById(id));
    }
}
