package com.blogapp.migration;

import com.blogapp.question.entity.Grade;
import com.blogapp.question.entity.Subject;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ChangeUnit(id = "seed-question-grades-subjects", order = "002", author = "system")
public class DatabaseChangeLog002 {

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        log.info("Starting Mongock migration 002: Seeding specific grades and subjects...");

        // We will clear existing question grades and subjects to avoid duplicates or old grouped data.
        mongoTemplate.remove(new Query(), Grade.class);
        mongoTemplate.remove(new Query(), Subject.class);

        // Grades 1 to 12
        List<Grade> gradesToInsert = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            gradesToInsert.add(Grade.builder().name("Grade " + i).order(i).build());
        }
        
        List<Grade> savedGrades = new ArrayList<>(mongoTemplate.insertAll(gradesToInsert));
        log.info("Inserted {} grades.", savedGrades.size());

        List<Subject> subjectsToInsert = new ArrayList<>();
        int subjectOrder = 1;

        for (Grade grade : savedGrades) {
            String gradeName = grade.getName();
            int gradeNum = Integer.parseInt(gradeName.replace("Grade ", ""));
            List<String> subjectNames = new ArrayList<>();

            if (gradeNum >= 1 && gradeNum <= 8) {
                // Primary (1-5) and Lower Secondary (6-8)
                subjectNames.addAll(Arrays.asList("Languages", "Mathematics", "Science / Environmental Studies", "Social Studies", "Computer Science"));
            } else if (gradeNum >= 9 && gradeNum <= 10) {
                // IGCSE (9-10)
                subjectNames.addAll(Arrays.asList("Languages", "Mathematics (Core or Extended)", "Science", "Physics", "Chemistry", "Biology", "Humanities and Social Studies", "Computer Science"));
            } else if (gradeNum >= 11 && gradeNum <= 12) {
                // AS Level (11) and A Level (12)
                subjectNames.addAll(Arrays.asList("Physics", "Chemistry", "Economics", "Mathematics", "Further Mathematics", "Languages", "Biology"));
            }

            for (String subName : subjectNames) {
                subjectsToInsert.add(Subject.builder()
                        .name(subName)
                        .gradeId(grade.getId())
                        .order(subjectOrder++)
                        .build());
            }
        }

        mongoTemplate.insertAll(subjectsToInsert);
        log.info("Inserted {} subjects mapped to grades 1-12.", subjectsToInsert.size());
        log.info("Database migration 002 completed successfully!");
    }

    @RollbackExecution
    public void rollback() {
        log.warn("Rollback triggered for seed-question-grades-subjects. No-op.");
    }
}
