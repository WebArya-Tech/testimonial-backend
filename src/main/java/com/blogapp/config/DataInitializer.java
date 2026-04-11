package com.blogapp.config;

import com.blogapp.demo.entity.Board;
import com.blogapp.demo.entity.Grade;
import com.blogapp.demo.repository.BoardRepository;
import com.blogapp.demo.repository.GradeRepository;
import com.blogapp.contact.entity.ContactSubject;
import com.blogapp.contact.repository.ContactSubjectRepository;
import com.blogapp.blog.entity.BlogPost;
import com.blogapp.blog.enums.BlogStatus;
import com.blogapp.blog.repository.BlogPostRepository;
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
    private final BoardRepository boardRepository;
    private final GradeRepository gradeRepository;
    private final ContactSubjectRepository contactSubjectRepository;
    private final BlogPostRepository blogPostRepository;

    @Override
    public void run(String... args) {
        log.info("Checking database state for initialization...");

        // ── Teachers ──
        if (teacherRepository.count() == 0) {
            List<Teacher> teachers = createSampleTeachers();
            teacherRepository.saveAll(teachers);
            log.info("Created {} sample teachers", teachers.size());

            // ── Testimonials (Depend on Teachers) ──
            if (testimonialRepository.count() == 0) {
                List<Testimonial> testimonials = createSampleTestimonials(teachers);
                testimonialRepository.saveAll(testimonials);
                log.info("Created {} sample testimonials", testimonials.size());
            }
        } else {
            log.info("Teachers/Testimonials collection already has data. Skipping.");
        }

        // ── Users ──
        if (userRepository.count() == 0) {
            List<User> users = createSampleUsers();
            userRepository.saveAll(users);
            log.info("Created {} sample users", users.size());
        } else {
            log.info("Users collection already has data. Skipping.");
        }

        // ── Boards ──
        if (boardRepository.count() == 0) {
            List<Board> boards = Arrays.asList(
                    Board.builder().name("CBSE").build(),
                    Board.builder().name("ICSE").build(),
                    Board.builder().name("IGCSE").build(),
                    Board.builder().name("IB").build(),
                    Board.builder().name("State Board").build()
            );
            boardRepository.saveAll(boards);
            log.info("Created {} sample boards", boards.size());
        } else {
            log.info("Boards collection already has data. Skipping.");
        }

        // ── Grades ──
        if (gradeRepository.count() == 0) {
            List<Grade> grades = Arrays.asList(
                    Grade.builder().name("Grade 6").build(),
                    Grade.builder().name("Grade 7").build(),
                    Grade.builder().name("Grade 8").build(),
                    Grade.builder().name("Grade 9").build(),
                    Grade.builder().name("Grade 10").build(),
                    Grade.builder().name("Grade 11").build(),
                    Grade.builder().name("Grade 12").build()
            );
            gradeRepository.saveAll(grades);
            log.info("Created {} sample grades", grades.size());
        } else {
            log.info("Grades collection already has data. Skipping.");
        }

        // ── Contact Subjects ──
        if (contactSubjectRepository.count() == 0) {
            List<ContactSubject> subjects = Arrays.asList(
                    ContactSubject.builder().name("Course Inquiry").build(),
                    ContactSubject.builder().name("Fees & Payments").build(),
                    ContactSubject.builder().name("Technical Support").build(),
                    ContactSubject.builder().name("Partnership").build(),
                    ContactSubject.builder().name("Other").build()
            );
            contactSubjectRepository.saveAll(subjects);
            log.info("Created {} sample contact subjects", subjects.size());
        } else {
            log.info("Contact Subjects collection already has data. Skipping.");
        }

        // ── Blogs ──
        if (blogPostRepository.count() == 0) {
            List<BlogPost> blogs = createSampleBlogs();
            blogPostRepository.saveAll(blogs);
            log.info("Created {} sample blogs", blogs.size());
        } else {
            log.info("Blogs collection already has data. Skipping.");
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

    private List<BlogPost> createSampleBlogs() {
        return Arrays.asList(
                BlogPost.builder()
                        .title("How to Prepare for IGCSE Physics")
                        .slug("how-to-prepare-for-igcse-physics")
                        .excerpt("A comprehensive guide to acing IGCSE Physics exams.")
                        .contentHtml("<p>IGCSE Physics can be challenging, but with the right approach, you can master it...</p>")
                        .featuredImageUrl("https://images.unsplash.com/photo-1532012197367-bf84dca3c2aa?w=800")
                        .authorName("Dr. Priya Sharma")
                        .authorEmail("priya@example.com")
                        .status(BlogStatus.PUBLISHED)
                        .publishedAt(LocalDateTime.now())
                        .year(LocalDateTime.now().getYear())
                        .month(LocalDateTime.now().getMonthValue())
                        .tags(Arrays.asList("physics", "igcse", "exam-prep"))
                        .viewsCount(150)
                        .likesCount(45)
                        .build(),
                BlogPost.builder()
                        .title("The Secrets of Organic Chemistry")
                        .slug("secrets-of-organic-chemistry")
                        .excerpt("Organic chemistry made simple with these 5 tricks.")
                        .contentHtml("<p>Many students fear organic chemistry, but it's all about understanding patterns...</p>")
                        .featuredImageUrl("https://images.unsplash.com/photo-1541339907198-e08759df9a65?w=800")
                        .authorName("Vikram Desai")
                        .authorEmail("vikram@example.com")
                        .status(BlogStatus.PUBLISHED)
                        .publishedAt(LocalDateTime.now().minusDays(2))
                        .year(LocalDateTime.now().minusDays(2).getYear())
                        .month(LocalDateTime.now().minusDays(2).getMonthValue())
                        .tags(Arrays.asList("chemistry", "organic", "study-tips"))
                        .viewsCount(85)
                        .likesCount(20)
                        .build(),
                BlogPost.builder()
                        .title("Top 10 English Literature Books")
                        .slug("top-10-english-literature-books")
                        .excerpt("Discover the classics that every student should read.")
                        .contentHtml("<p>Literature opens up new worlds. Here are our top 10 picks...</p>")
                        .featuredImageUrl("https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=800")
                        .authorName("Ananya Iyer")
                        .authorEmail("ananya@example.com")
                        .status(BlogStatus.PENDING)
                        .submittedAt(LocalDateTime.now().minusHours(5))
                        .tags(Arrays.asList("english", "literature", "books"))
                        .build()
        );
    }
}
