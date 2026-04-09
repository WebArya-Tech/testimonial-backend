package com.blogapp.contact.controller;

import com.blogapp.contact.dto.request.ContactMessageRequest;
import com.blogapp.contact.dto.response.ContactMessageResponse;
import com.blogapp.contact.dto.response.SubjectResponse;
import com.blogapp.contact.service.ContactMessageService;
import com.blogapp.contact.service.ContactSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/contact")
@RequiredArgsConstructor
@Tag(name = "Public Contact Us", description = "Endpoints for the Send Us a Message form")
public class PublicContactController {

    private final ContactSettingsService settingsService;
    private final ContactMessageService messageService;

    @GetMapping("/subjects")
    @Operation(summary = "Get all active subjects for the contact dropdown")
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        return ResponseEntity.ok(settingsService.getAllSubjects());
    }

    @PostMapping("/message")
    @Operation(summary = "Submit a contact us message (No OTP required)")
    public ResponseEntity<ContactMessageResponse> submitMessage(
            @Valid @RequestBody ContactMessageRequest request) {
        ContactMessageResponse response = messageService.submitMessage(request);
        return ResponseEntity.ok(response);
    }
}
