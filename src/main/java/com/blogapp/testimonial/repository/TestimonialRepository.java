package com.blogapp.testimonial.repository;

import com.blogapp.testimonial.entity.Testimonial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestimonialRepository extends MongoRepository<Testimonial, String> {

    List<Testimonial> findByTeacherId(String teacherId);

    Page<Testimonial> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Testimonial> findAllByOrderByCreatedAtDesc();

    List<Testimonial> findByIsPrimaryTrue();

    List<Testimonial> findByTeacherIdAndIsPrimaryTrue(String teacherId);
}
