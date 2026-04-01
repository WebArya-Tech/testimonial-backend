package com.blogapp.config;

import com.blogapp.teacher.entity.Teacher;
import com.blogapp.teacher.repository.TeacherRepository;
import com.blogapp.testimonial.entity.Testimonial;
import com.blogapp.testimonial.repository.TestimonialRepository;
import com.blogapp.user.entity.User;
import com.blogapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final TestimonialRepository testimonialRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (teacherRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }

        log.info("Initializing database with seed data...");

        // ── Teachers ──
        List<Teacher> teachers = createSampleTeachers();
        teacherRepository.saveAll(teachers);
        log.info("Created {} sample teachers", teachers.size());

        // ── Testimonials ──
        List<Testimonial> testimonials = createSampleTestimonials(teachers);
        testimonialRepository.saveAll(testimonials);
        log.info("Created {} sample testimonials", testimonials.size());

        // ── Users ──
        if (userRepository.count() == 0) {
            List<User> users = createSampleUsers();
            userRepository.saveAll(users);
            log.info("Created {} sample users", users.size());
        }

        log.info("Database initialization completed successfully!");
    }

    private List<Teacher> createSampleTeachers() {
        return Arrays.asList(
                Teacher.builder()
                        .fullName("Dr. Priya Sharma")
                        .bio("Senior Mathematics Coach with 15+ years of experience in competitive exam preparation.")
                        .photoUrl("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400")
                        .category("Science")
                        .speciality("Mathematics")
                        .mainSubject("Maths")
                        .build(),
                Teacher.builder()
                        .fullName("Rajesh Kumar")
                        .bio("Physics expert and mentor. IIT alumnus passionate about making physics intuitive and fun.")
                        .photoUrl("https://images.unsplash.com/photo-1560250097-0b93528c311a?w=400")
                        .category("Science")
                        .speciality("Physics")
                        .mainSubject("Physics")
                        .build(),
                Teacher.builder()
                        .fullName("Ananya Iyer")
                        .bio("English language and communication coach. Specializes in IELTS, TOEFL, and public speaking.")
                        .photoUrl("https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400")
                        .category("Language")
                        .speciality("English & Communication")
                        .mainSubject("English")
                        .build(),
                Teacher.builder()
                        .fullName("Vikram Desai")
                        .bio("Chemistry coach with a knack for simplifying organic chemistry. 10+ years of coaching experience.")
                        .photoUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400")
                        .category("Science")
                        .speciality("Chemistry")
                        .mainSubject("Chemistry")
                        .build()
        );
    }

    private List<Testimonial> createSampleTestimonials(List<Teacher> teachers) {
        return Arrays.asList(
                // TEXT review — approved & primary
                Testimonial.builder()
                        .text("Dr. Priya is an outstanding math teacher! Her coaching helped me score 98% in my board exams. The way she breaks down complex problems is simply brilliant.")
                        .mediaUrl("")
                        .isPrimary(true)
                        .build(),

                // TEXT review — approved
                Testimonial.builder()
                        .text("Rajesh sir makes physics so easy to understand. His real-world examples and experiments made the subject come alive for me.")
                        .mediaUrl("")
                        .build(),

                // URL (video) review — approved & primary
                Testimonial.builder()
                        .text("Amazing practicals in class today!")
                        .mediaUrl("https://res.cloudinary.com/demo/video/upload/v1234567890/testimonials/karan-review.mp4")
                        .isPrimary(true)
                        .build(),

                // TEXT review — pending
                Testimonial.builder()
                        .text("Ananya ma'am's IELTS coaching was life-changing. I got a band 8.5 on my first attempt thanks to her guidance!")
                        .mediaUrl("")
                        .build(),

                // URL (image) review — approved
                Testimonial.builder()
                        .text("Got my certificate directly signed!")
                        .mediaUrl("https://res.cloudinary.com/demo/image/upload/v1234567890/testimonials/rohit-certificate.jpg")
                        .build(),

                // TEXT review — rejected
                Testimonial.builder()
                        .text("spam content")
                        .mediaUrl("")
                        .build()
        );
    }

    private List<User> createSampleUsers() {
        return Arrays.asList(
                User.builder()
                        .name("Alice Viewer")
                        .email("alice@example.com")
                        .emailVerifiedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build(),
                User.builder()
                        .name("Bob Student")
                        .email("bob@example.com")
                        .emailVerifiedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
