package com.blogapp.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelDemoRequest {
    @NotBlank(message = "Cancellation reason is required")
    private String cancelReason;
}
