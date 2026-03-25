package com.blogapp.admin.seeder;

import com.blogapp.admin.entity.Admin;
import com.blogapp.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String defaultAdminEmail;

    @Value("${app.admin.password:admin123}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) {
        if (!adminRepository.existsByEmail(defaultAdminEmail)) {
            log.info("No default admin found in DB. Automatically creating super-admin...");
            
            Admin admin = Admin.builder()
                    .email(defaultAdminEmail)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .createdAt(LocalDateTime.now())
                    .build();
            
            adminRepository.save(admin);
            log.info("Super-admin created with email: {}", defaultAdminEmail);
        } else {
            log.info("Admin already exists in DB. Skipping seeder.");
        }
    }
}
