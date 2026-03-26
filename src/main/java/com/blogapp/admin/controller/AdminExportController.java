package com.blogapp.admin.controller;

import com.blogapp.common.service.CsvExportService;
import com.blogapp.testimonial.entity.Testimonial;
import com.blogapp.testimonial.repository.TestimonialRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/export")
@RequiredArgsConstructor
@Tag(name = "Admin Export", description = "Extracts database records into CSV offline formatting")
public class AdminExportController {

    private final CsvExportService csvExportService;
    private final TestimonialRepository testimonialRepository;

    @GetMapping("/testimonials")
    @Operation(summary = "Export all testimonials to CSV", description = "Downloads a spreadsheet containing all testimonials. Notice how the CSV logic is entirely decoupled from the entity.")
    public void exportTestimonialsToCsv(HttpServletResponse response) {
        
        // 1. Fetch data from anywhere
        List<Testimonial> data = testimonialRepository.findAll();

        // 2. Define exactly what goes in the columns
        String[] headers = {"Testimonial ID", "User Email", "User Name", "Type", "Content"};

        // 3. Tell the generic service HOW to map the entity into the columns
        csvExportService.exportCsvToResponse(
                response, 
                "testimonials_export.csv", 
                headers, 
                data, 
                (Testimonial t) -> new String[]{
                        t.getId(),
                        t.getReviewerEmail(),
                        t.getReviewerName(),
                        t.getType().name(),
                        t.getContent()
                }
        );
    }
}
