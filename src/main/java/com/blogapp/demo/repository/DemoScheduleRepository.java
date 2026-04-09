package com.blogapp.demo.repository;

import com.blogapp.demo.entity.DemoSchedule;
import com.blogapp.demo.enums.DemoScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DemoScheduleRepository extends MongoRepository<DemoSchedule, String> {
    Page<DemoSchedule> findByStatus(DemoScheduleStatus status, Pageable pageable);
    Page<DemoSchedule> findByPreferredDate(LocalDate preferredDate, Pageable pageable);
    Page<DemoSchedule> findByStatusAndPreferredDate(DemoScheduleStatus status, LocalDate preferredDate, Pageable pageable);
}
