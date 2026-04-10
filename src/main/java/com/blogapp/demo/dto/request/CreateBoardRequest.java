package com.blogapp.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBoardRequest {
    @NotBlank(message = "Board name is required")
    @Schema(description = "Name of the board", example = "IGCSE")
    private String name;
}
