package com.blogapp.contact.service;

import com.blogapp.contact.dto.request.CreateSubjectRequest;
import com.blogapp.contact.dto.response.SubjectResponse;

import java.util.List;

public interface ContactSettingsService {

    SubjectResponse createSubject(CreateSubjectRequest request);

    List<SubjectResponse> getAllSubjects();

    void deleteSubject(String id);
}
