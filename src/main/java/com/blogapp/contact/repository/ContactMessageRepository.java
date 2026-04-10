package com.blogapp.contact.repository;

import com.blogapp.contact.entity.ContactMessage;
import com.blogapp.contact.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends MongoRepository<ContactMessage, String> {
    Page<ContactMessage> findByStatus(ContactStatus status, Pageable pageable);
}
