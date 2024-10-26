package com.achivhub.backend.persistence.collection;

import com.achivhub.backend.domain.model.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementCollection {
    Achievement save(Achievement achievement);

    Achievement findAchievementById(String id);

    Page<Achievement> getAllAchievements(int page, int size, String sortBy, String sortDirection, String name, List<String> category, List<String> status, String time, String userIdf, String title);

    Boolean deleteAchievementById(String id);

    Page<Achievement> getAllAchievementsByUserId(String userId, int page, int size, String sortBy, String sortDirection, String name, List<String> category, List<String> status, String time, String userIdf, String title);
}
