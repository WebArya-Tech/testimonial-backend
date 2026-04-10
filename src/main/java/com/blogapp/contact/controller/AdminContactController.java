package com.blogapp.contact.controller;

import com.blogapp.contact.dto.request.CreateSubjectRequest;
import com.blogapp.contact.dto.response.ContactMessageResponse;
import com.blogapp.contact.dto.response.SubjectResponse;
import com.blogapp.contact.enums.ContactStatus;
import com.blogapp.contact.service.ContactMessageService;
import com.blogapp.contact.service.ContactSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/contact")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Contact Settings", description = "Endpoints for admin to manage contact subjects and review messages")
public class AdminContactController {

    private final ContactSettingsService settingsService;
    private final ContactMessageService messageService;

    // --- Subjects ---

    @PostMapping("/subjects")
    @Operation(summary = "Create a new contact subject for the dropdown")
    public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody CreateSubjectRequest request) {
        return ResponseEntity.ok(settingsService.createSubject(request));
    }

    @DeleteMapping("/subjects/{id}")
    @Operation(summary = "Delete a contact subject")
    public ResponseEntity<Map<String, String>> deleteSubject(@PathVariable String id) {
        settingsService.deleteSubject(id);
        return ResponseEntity.ok(Map.of("message", "Subject deleted successfully"));
    }

    // --- Messages ---

    @GetMapping("/messages")
    @Operation(summary = "View paginated and filtered contact messages")
    public ResponseEntity<Page<ContactMessageResponse>> getMessages(
            @RequestParam(required = false) ContactStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Page<ContactMessageResponse> messages = messageService.getMessages(status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/messages/{id}/status")
    @Operation(summary = "Update status of a contact message (e.g., READ or RESOLVED)")
    public ResponseEntity<ContactMessageResponse> updateMessageStatus(
            @PathVariable String id,
            @RequestParam ContactStatus status) {
        ContactMessageResponse response = messageService.updateMessageStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/messages/{id}")
    @Operation(summary = "Delete an old contact message permanently")
    public ResponseEntity<Map<String, String>> deleteMessage(@PathVariable String id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(Map.of("message", "Contact Message deleted successfully"));
    }
}
