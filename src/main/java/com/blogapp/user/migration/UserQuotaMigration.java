package com.blogapp.user.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserQuotaMigration implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting User Quota DB Migration...");

        // Find users where isEnrolled or freeAskOrAnswerCount fields do not exist
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("isEnrolled").exists(false),
                Criteria.where("freeAskOrAnswerCount").exists(false)
        ));

        // Set default values without breaking existing data
        Update update = new Update()
                .setOnInsert("isEnrolled", false)
                .setOnInsert("freeAskOrAnswerCount", 0);
        
        // Use updateMulti to update all matching documents
        // Using $set explicitly for existing documents missing these fields to ensure consistency
        Update setUpdate = new Update()
                .set("isEnrolled", false)
                .set("freeAskOrAnswerCount", 0);

        var result = mongoTemplate.updateMulti(query, setUpdate, "users");
        
        log.info("Completed User Quota DB Migration. Updated {} users.", result.getModifiedCount());
    }
}
