package com.achivhub.backend.persistence.adapter;

import com.achivhub.backend.domain.mappers.AchievementMapper;
import com.achivhub.backend.domain.model.Achievement;
import com.achivhub.backend.persistence.database.AchievementMongoCollection;
import com.achivhub.backend.persistence.documents.AchievementDocument;
import com.achivhub.backend.persistence.collection.AchievementCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AchievementAdapter implements AchievementCollection {

    private final AchievementMongoCollection achievementMongoCollection;
    private final AchievementMapper achievementMapper;
    private final MongoOperations mongoOperations;

    @Override
    public Achievement save(Achievement achievement) {
        AchievementDocument document = achievementMapper.modelToDocument(achievement);
        return achievementMapper.documentToModel(
                achievementMongoCollection.save(document)
        );
    }

    @Override
    public Achievement findAchievementById(String id) {
        if(achievementMongoCollection.findById(id).isEmpty()) {
           throw new NoSuchElementException("No Achievement found with id: " + id);
        }
        return achievementMapper.documentToModel(achievementMongoCollection.findById(id).get());
    }

    @Override
    public Page<Achievement> getAllAchievements(int page, int size, String sortBy, String sortDirection, String name, List<String> category, List<String> status, String time, String userIdf, String title) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Criteria criteria = buildCriteria(name, category, status, time, userIdf, title);
        Query query = new Query(criteria);

        long total = mongoOperations.count(query, AchievementDocument.class);
        query.with(pageable);

        List<AchievementDocument> documents = mongoOperations.find(query, AchievementDocument.class);

        List<Achievement> achievements = documents
                .stream()
                .map(achievementMapper::documentToModel)
                .toList();

        return new PageImpl<>(achievements, pageable, total);
    }

    @Override
    public Boolean deleteAchievementById(String id) {
        achievementMongoCollection.deleteById(id);
        return true;
    }

    @Override
    public Page<Achievement> getAllAchievementsByUserId(String userId, int page, int size, String sortBy, String sortDirection, String name, List<String> category, List<String> status, String time, String userIdf, String title) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Criteria criteria = buildCriteria(name, category, status, time, userIdf, title);
        criteria = criteria.and("userId").is(userId);
        Query query = new Query(criteria);

        long total = mongoOperations.count(query, AchievementDocument.class);
        query.with(pageable);

        List<AchievementDocument> documents = mongoOperations.find(query, AchievementDocument.class);

        List<Achievement> achievements = documents
                .stream()
                .map(achievementMapper::documentToModel)
                .toList();

        return new PageImpl<>(achievements, pageable, total);
    }

    private Criteria buildCriteria(String name, List<String> category, List<String> status, String time, String userIdf, String title) {
        Criteria criteria = new Criteria();
        if (name != null && !name.isEmpty()) {
            criteria = criteria.and("name").regex(name, "i");
        }

        if (category != null && !category.isEmpty()) {
            criteria = criteria.and("category").in(category);
        }

        if (status != null && !status.isEmpty()) {
            criteria = criteria.and("status").in(status);
        }

//        if (time != null && !time.isEmpty()) {
//            try {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
//                ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, formatter);
//                LocalDateTime inputDateTime = zonedDateTime.toLocalDateTime();
//                LocalDateTime endOfDay = inputDateTime.toLocalDate().atTime(LocalTime.MAX);
//
//                log.info("end of day time {}", endOfDay);
//                log.info("input date {}", inputDateTime);
//                criteria = Criteria.where("time").gte(inputDateTime);
//
//            } catch (DateTimeParseException e) {
//                throw new IllegalArgumentException("Invalid date format: " + time, e);
//            }
//        }

        if (userIdf != null && !userIdf.isEmpty()) {
            criteria = criteria.and("userId").is(userIdf);
        }

        if (title != null && !title.isEmpty()) {
            criteria = criteria.and("title").regex(title, "i");
        }

        return criteria;
    }
}
