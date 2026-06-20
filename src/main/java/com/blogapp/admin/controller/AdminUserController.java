package com.blogapp.admin.controller;

import com.blogapp.user.entity.User;
import com.blogapp.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin - Users", description = "User management including Enrollment")
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/{userId}/enroll")
    @Operation(summary = "Enroll a student", description = "Marks user as enrolled and updates their grade/mobile if provided")
    public ResponseEntity<User> enrollStudent(@PathVariable String userId, @RequestBody EnrollmentRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEnrolled(true);
        if (request.getGrade() != null) user.setGrade(request.getGrade());
        if (request.getMobile() != null) user.setMobile(request.getMobile());
        if (request.getName() != null) user.setName(request.getName());
        return ResponseEntity.ok(userRepository.save(user));
    }

    @Data
    public static class EnrollmentRequest {
        private String grade;
        private String mobile;
        private String name;
    }
}
