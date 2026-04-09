package com.blogapp.contact.service.impl;

import com.blogapp.common.exception.ResourceNotFoundException;
import com.blogapp.contact.dto.request.CreateSubjectRequest;
import com.blogapp.contact.dto.response.SubjectResponse;
import com.blogapp.contact.entity.ContactSubject;
import com.blogapp.contact.mapper.ContactMapper;
import com.blogapp.contact.repository.ContactSubjectRepository;
import com.blogapp.contact.service.ContactSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactSettingsServiceImpl implements ContactSettingsService {

    private final ContactSubjectRepository subjectRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public SubjectResponse createSubject(CreateSubjectRequest request) {
        ContactSubject subject = ContactSubject.builder()
                .name(request.getName())
                .build();
        return contactMapper.toSubjectResponse(subjectRepository.save(subject));
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(contactMapper::toSubjectResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSubject(String id) {
        ContactSubject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        subjectRepository.delete(subject);
    }
}
