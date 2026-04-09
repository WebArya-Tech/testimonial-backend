package com.blogapp.demo.entity;

import com.blogapp.demo.enums.DemoScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "demo_schedules")
public class DemoSchedule {

    @Id
    private String id;

    private String studentName;
    private String parentName;
    private String emailId;
    private String mobileNumber;

    private String boardId;
    private String gradeId;

    private LocalDate preferredDate;
    private String preferredTime;

    @Builder.Default
    private DemoScheduleStatus status = DemoScheduleStatus.PENDING;

    private String cancelReason;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
