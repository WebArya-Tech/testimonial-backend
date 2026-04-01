package com.blogapp.admin.seeder;

import com.blogapp.answer.entity.Answer;
import com.blogapp.answer.enums.AnswerStatus;
import com.blogapp.answer.repository.AnswerRepository;
import com.blogapp.category.entity.Category;
import com.blogapp.category.repository.CategoryRepository;
import com.blogapp.question.entity.Question;
import com.blogapp.question.repository.QuestionRepository;
import com.blogapp.teacher.entity.Teacher;
import com.blogapp.teacher.repository.TeacherRepository;
import com.blogapp.testimonial.entity.Testimonial;
import com.blogapp.testimonial.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataSeeder implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final TestimonialRepository testimonialRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public void run(String... args) {
        // Only run if the database is completely empty for Teachers
        if (teacherRepository.count() == 0) {
            log.info("No Teachers found... Seeding dummy Ecosystem data.");
            seedEcosystem();
            log.info("Finished seeding dummy data successfully.");
        } else {
            log.info("Database already contains records. Skipping DummyDataSeeder.");
        }
    }

    private void seedEcosystem() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Seed Teacher
        Teacher mathTeacher = Teacher.builder()
                .fullName("Prof. Alan Math")
                .bio("Advanced Calculus & Algebra Specialist with 10 years at MIT.")
                .photoUrl("https://ui-avatars.com/api/?name=Alan+Math&background=random")
                .category("Science")
                .speciality("Mathematics")
                .mainSubject("Calculus")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        mathTeacher = teacherRepository.save(mathTeacher);

        // 2. Seed Testimonials for Teacher
        Testimonial textReview = Testimonial.builder()
                .text("Prof. Math is incredible! He broke down integrals effortlessly.")
                .mediaUrl("")
                .isPrimary(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        Testimonial videoReview = Testimonial.builder()
                .text("This visual representation really helped my understanding!")
                .mediaUrl("https://res.cloudinary.com/demo/video/upload/elephants.mp4")
                .isPrimary(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        testimonialRepository.save(textReview);
        testimonialRepository.save(videoReview);

        // 3. Seed Category
        Category category = Category.builder()
                .name("Calculus")
                .slug("calculus")
                .createdAt(now)
                .updatedAt(now)
                .build();
        category = categoryRepository.save(category);

        // 4. Seed Questions
        Question q1 = Question.builder()
                .title("What is the integral of x^2?")
                .slug("what-is-integral-x2")
                .descriptionHtml("<p>I am struggling with inverse power rules. Please help explain the calculus behind <strong>x<sup>2</sup></strong> integration.</p>")
                .categoryId(category.getId())
                .adminId("seed-admin-123")
                .createdAt(now)
                .updatedAt(now)
                .build();
        q1 = questionRepository.save(q1);

        // 5. Seed Answers
        Answer approvedAnswer = Answer.builder()
                .questionId(q1.getId())
                .userId("seed-user-123")
                .authorName("Charlie Tutor")
                .contentHtml("<p>The integral of x<sup>2</sup> is exactly <strong>x<sup>3</sup> / 3 + C</strong>.</p>")
                .status(AnswerStatus.APPROVED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Answer pendingAnswer = Answer.builder()
                .questionId(q1.getId())
                .userId("seed-user-456")
                .authorName("Dave Newbie")
                .contentHtml("<p>I think you just divide by 2?</p>")
                .status(AnswerStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        answerRepository.save(approvedAnswer);
        answerRepository.save(pendingAnswer);
    }
}
