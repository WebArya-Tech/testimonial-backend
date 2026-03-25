package com.blogapp.admin.controller;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.teacher.dto.request.CreateTeacherRequest;
import com.blogapp.teacher.dto.request.UpdateTeacherRequest;
import com.blogapp.teacher.dto.response.TeacherResponse;
import com.blogapp.teacher.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
@Tag(name = "Admin - Teachers", description = "Admin endpoints for full teacher CRUD")
public class AdminTeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @Operation(summary = "Create a teacher")
    public ResponseEntity<TeacherResponse> createTeacher(
            @Valid @RequestBody CreateTeacherRequest request) {
        return ResponseEntity.ok(teacherService.create(request));
    }

    @GetMapping
    @Operation(summary = "List all teachers (paginated)")
    public ResponseEntity<PageResponse<TeacherResponse>> getAllTeachers(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teacherService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<TeacherResponse> getTeacherById(
            @Parameter(description = "Teacher ID") @PathVariable String id) {
        return ResponseEntity.ok(teacherService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a teacher")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @Parameter(description = "Teacher ID") @PathVariable String id,
            @Valid @RequestBody UpdateTeacherRequest request) {
        return ResponseEntity.ok(teacherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a teacher")
    public ResponseEntity<Map<String, String>> deleteTeacher(
            @Parameter(description = "Teacher ID") @PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Teacher deleted successfully"));
    }
}
