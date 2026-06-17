package com.blogapp.admin.controller;

import com.blogapp.user.entity.Lead;
import com.blogapp.user.repository.LeadRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/leads")
@RequiredArgsConstructor
@Tag(name = "Admin - Leads", description = "View submitted leads from Visitors")
public class AdminLeadController {

    private final LeadRepository leadRepository;

    @GetMapping
    @Operation(summary = "Get all verified leads")
    public ResponseEntity<List<Lead>> getAllLeads() {
        return ResponseEntity.ok(leadRepository.findAll());
    }
}
