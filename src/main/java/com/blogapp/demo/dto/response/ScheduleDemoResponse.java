package com.blogapp.demo.dto.response;

import com.blogapp.demo.enums.DemoScheduleStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleDemoResponse {
    private String id;

    private String studentName;
    private String parentName;
    private String emailId;
    private String mobileNumber;

    private String boardId;
    private BoardResponse board;

    private String gradeId;
    private GradeResponse grade;

    private LocalDate preferredDate;
    private String preferredTime;

    private DemoScheduleStatus status;
    private String cancelReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
