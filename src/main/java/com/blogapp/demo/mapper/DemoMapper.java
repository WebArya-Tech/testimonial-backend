package com.blogapp.demo.mapper;

import com.blogapp.demo.dto.response.BoardResponse;
import com.blogapp.demo.dto.response.GradeResponse;
import com.blogapp.demo.dto.response.ScheduleDemoResponse;
import com.blogapp.demo.entity.Board;
import com.blogapp.demo.entity.DemoSchedule;
import com.blogapp.demo.entity.Grade;
import org.springframework.stereotype.Component;

@Component
public class DemoMapper {

    public BoardResponse toBoardResponse(Board board) {
        if (board == null) return null;
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public GradeResponse toGradeResponse(Grade grade) {
        if (grade == null) return null;
        return GradeResponse.builder()
                .id(grade.getId())
                .name(grade.getName())
                .createdAt(grade.getCreatedAt())
                .build();
    }

    public ScheduleDemoResponse toScheduleDemoResponse(DemoSchedule schedule, Board board, Grade grade) {
        if (schedule == null) return null;
        return ScheduleDemoResponse.builder()
                .id(schedule.getId())
                .studentName(schedule.getStudentName())
                .parentName(schedule.getParentName())
                .emailId(schedule.getEmailId())
                .mobileNumber(schedule.getMobileNumber())
                .boardId(schedule.getBoardId())
                .board(toBoardResponse(board))
                .gradeId(schedule.getGradeId())
                .grade(toGradeResponse(grade))
                .preferredDate(schedule.getPreferredDate())
                .preferredTime(schedule.getPreferredTime())
                .status(schedule.getStatus())
                .cancelReason(schedule.getCancelReason())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
