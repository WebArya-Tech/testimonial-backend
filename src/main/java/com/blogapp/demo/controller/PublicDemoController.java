package com.blogapp.demo.controller;

import com.blogapp.demo.dto.request.ScheduleDemoRequest;
import com.blogapp.demo.dto.request.SendOtpRequest;
import com.blogapp.demo.dto.response.BoardResponse;
import com.blogapp.demo.dto.response.GradeResponse;
import com.blogapp.demo.dto.response.ScheduleDemoResponse;
import com.blogapp.demo.service.DemoScheduleService;
import com.blogapp.demo.service.DemoSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/demo")
@RequiredArgsConstructor
@Tag(name = "Public Demo", description = "Endpoints for public demo scheduling and OTP")
public class PublicDemoController {

    private final DemoSettingsService settingsService;
    private final DemoScheduleService scheduleService;

    @GetMapping("/settings/boards")
    @Operation(summary = "Get all active boards for dropdown")
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        return ResponseEntity.ok(settingsService.getAllBoards());
    }

    @GetMapping("/settings/grades")
    @Operation(summary = "Get all active grades for dropdown")
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
        return ResponseEntity.ok(settingsService.getAllGrades());
    }

    @PostMapping("/schedule/send-otp")
    @Operation(summary = "Send OTP to user's email for demo scheduling")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        scheduleService.sendOtp(request);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully to email"));
    }

    @PostMapping("/schedule")
    @Operation(summary = "Submit demo schedule with OTP verification")
    public ResponseEntity<ScheduleDemoResponse> submitScheduleDemo(
            @Valid @RequestBody ScheduleDemoRequest request) {
        ScheduleDemoResponse response = scheduleService.submitScheduleDemo(request);
        return ResponseEntity.ok(response);
    }
}
