package com.blogapp.testimonial.service;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.testimonial.dto.request.SubmitTestimonialRequest;
import com.blogapp.testimonial.dto.response.TestimonialResponse;

import java.util.List;

public interface TestimonialService {

    TestimonialResponse submit(SubmitTestimonialRequest request);

    List<TestimonialResponse> getApprovedByTeacher(String teacherId);

    List<TestimonialResponse> getAllApproved();

    List<TestimonialResponse> getPrimaryTestimonials();

    PageResponse<TestimonialResponse> getAll(String status, int page, int size);

    TestimonialResponse approve(String id, String adminId);

    TestimonialResponse reject(String id, String reason);

    TestimonialResponse setPrimary(String id);

    void delete(String id);
}
