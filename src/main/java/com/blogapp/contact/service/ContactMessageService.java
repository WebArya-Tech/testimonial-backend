package com.blogapp.contact.service;

import com.blogapp.contact.dto.request.ContactMessageRequest;
import com.blogapp.contact.dto.response.ContactMessageResponse;
import com.blogapp.contact.enums.ContactStatus;
import org.springframework.data.domain.Page;

public interface ContactMessageService {

    ContactMessageResponse submitMessage(ContactMessageRequest request);

    Page<ContactMessageResponse> getMessages(ContactStatus status, int page, int size, String sortBy, String sortDir);

    ContactMessageResponse updateMessageStatus(String id, ContactStatus status);
    
    void deleteMessage(String id);
}
