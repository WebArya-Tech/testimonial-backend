package com.blogapp.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGradeRequest {
    @NotBlank(message = "Grade name is required")
    @Schema(description = "Name of the grade", example = "Grade 9")
    private String name;
}
