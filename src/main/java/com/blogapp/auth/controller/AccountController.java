package com.blogapp.auth.controller;

import com.blogapp.auth.dto.response.AuthResponse;
import com.blogapp.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Logged-in user endpoints")
public class AccountController {

    @GetMapping("/me")
    @Operation(summary = "Get my profile", description = "Returns the authenticated user's profile info")
    public ResponseEntity<AuthResponse.UserInfo> getMyProfile(@AuthenticationPrincipal User user) {
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .emailVerified(user.getEmailVerifiedAt() != null)
                .build();

        return ResponseEntity.ok(userInfo);
    }
}
