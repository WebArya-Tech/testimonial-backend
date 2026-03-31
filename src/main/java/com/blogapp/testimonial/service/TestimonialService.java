package com.blogapp.testimonial.service;

import com.blogapp.common.dto.PageResponse;
import com.blogapp.testimonial.dto.request.SubmitTestimonialRequest;
import com.blogapp.testimonial.dto.response.TestimonialResponse;

import java.util.List;

public interface TestimonialService {

    TestimonialResponse submit(SubmitTestimonialRequest request);

    TestimonialResponse update(String id, SubmitTestimonialRequest request);

    List<TestimonialResponse> getAll();

    List<TestimonialResponse> getPrimaryTestimonials();

    PageResponse<TestimonialResponse> getPaginated(int page, int size);

    TestimonialResponse setPrimary(String id);

    void delete(String id);
}
