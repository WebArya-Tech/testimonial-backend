package com.blogapp.demo.service;

import com.blogapp.demo.dto.request.CancelDemoRequest;
import com.blogapp.demo.dto.request.ScheduleDemoRequest;
import com.blogapp.demo.dto.request.SendOtpRequest;
import com.blogapp.demo.dto.response.ScheduleDemoResponse;
import com.blogapp.demo.enums.DemoScheduleStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface DemoScheduleService {

    void sendOtp(SendOtpRequest request);

    ScheduleDemoResponse submitScheduleDemo(ScheduleDemoRequest request);

    Page<ScheduleDemoResponse> getSchedules(LocalDate date, DemoScheduleStatus status, int page, int size, String sortBy, String sortDir);

    ScheduleDemoResponse approveSchedule(String id);

    ScheduleDemoResponse cancelSchedule(String id, CancelDemoRequest request);
}
