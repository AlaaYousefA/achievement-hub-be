package com.achivhub.backend.persistence.database;

import com.achivhub.backend.persistence.documents.AchievementDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementMongoCollection extends MongoRepository<AchievementDocument, String> {
    Page<AchievementDocument> findAllByUserId(String userId, Pageable pageable);
}
