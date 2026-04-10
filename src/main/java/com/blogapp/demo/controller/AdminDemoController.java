package com.blogapp.demo.controller;

import com.blogapp.demo.dto.request.CancelDemoRequest;
import com.blogapp.demo.dto.request.CreateBoardRequest;
import com.blogapp.demo.dto.request.CreateGradeRequest;
import com.blogapp.demo.dto.response.BoardResponse;
import com.blogapp.demo.dto.response.GradeResponse;
import com.blogapp.demo.dto.response.ScheduleDemoResponse;
import com.blogapp.demo.enums.DemoScheduleStatus;
import com.blogapp.demo.service.DemoScheduleService;
import com.blogapp.demo.service.DemoSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/demo")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Demo Settings", description = "Endpoints for admin to manage demo boards, grades, and schedules")
public class AdminDemoController {

    private final DemoSettingsService settingsService;
    private final DemoScheduleService scheduleService;

    // --- Boards ---

    @PostMapping("/settings/boards")
    @Operation(summary = "Create a new board offering")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.ok(settingsService.createBoard(request));
    }

    @DeleteMapping("/settings/boards/{id}")
    @Operation(summary = "Delete an existing board offering")
    public ResponseEntity<Map<String, String>> deleteBoard(@PathVariable String id) {
        settingsService.deleteBoard(id);
        return ResponseEntity.ok(Map.of("message", "Board deleted successfully"));
    }

    // --- Grades ---

    @PostMapping("/settings/grades")
    @Operation(summary = "Create a new grade offering")
    public ResponseEntity<GradeResponse> createGrade(@Valid @RequestBody CreateGradeRequest request) {
        return ResponseEntity.ok(settingsService.createGrade(request));
    }

    @DeleteMapping("/settings/grades/{id}")
    @Operation(summary = "Delete an existing grade offering")
    public ResponseEntity<Map<String, String>> deleteGrade(@PathVariable String id) {
        settingsService.deleteGrade(id);
        return ResponseEntity.ok(Map.of("message", "Grade deleted successfully"));
    }

    // --- Schedule Demo ---

    @GetMapping("/schedule")
    @Operation(summary = "Get list of demo schedules with optional filters")
    public ResponseEntity<Page<ScheduleDemoResponse>> getSchedules(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) DemoScheduleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Page<ScheduleDemoResponse> schedules = scheduleService.getSchedules(date, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/schedule/{id}/approve")
    @Operation(summary = "Approve a pending demo schedule")
    public ResponseEntity<ScheduleDemoResponse> approveSchedule(@PathVariable String id) {
        ScheduleDemoResponse response = scheduleService.approveSchedule(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedule/{id}/cancel")
    @Operation(summary = "Cancel a demo schedule with a reason")
    public ResponseEntity<ScheduleDemoResponse> cancelSchedule(
            @PathVariable String id,
            @Valid @RequestBody CancelDemoRequest request) {
        ScheduleDemoResponse response = scheduleService.cancelSchedule(id, request);
        return ResponseEntity.ok(response);
    }
}
