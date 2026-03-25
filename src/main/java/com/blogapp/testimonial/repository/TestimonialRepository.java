package com.blogapp.testimonial.repository;

import com.blogapp.testimonial.entity.Testimonial;
import com.blogapp.testimonial.enums.TestimonialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestimonialRepository extends MongoRepository<Testimonial, String> {

    List<Testimonial> findByTeacherIdAndStatus(String teacherId, TestimonialStatus status);

    Page<Testimonial> findByStatus(TestimonialStatus status, Pageable pageable);

    List<Testimonial> findByStatusOrderByCreatedAtDesc(TestimonialStatus status);

    List<Testimonial> findByIsPrimaryTrueAndStatus(TestimonialStatus status);

    List<Testimonial> findByTeacherIdAndIsPrimaryTrue(String teacherId);
}
