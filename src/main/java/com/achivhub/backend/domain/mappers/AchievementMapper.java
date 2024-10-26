package com.achivhub.backend.domain.mappers;

import com.achivhub.backend.application.dtos.achievement.AchievementRequest;
import com.achivhub.backend.application.dtos.achievement.AchievementResponse;
import com.achivhub.backend.domain.model.Achievement;
import com.achivhub.backend.persistence.documents.AchievementDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AchievementMapper {
    Achievement requestToModel(AchievementRequest achievementRequest);
    AchievementResponse modelToResponse(Achievement achievement);
    Achievement documentToModel(AchievementDocument achievementDocument);
    AchievementDocument modelToDocument(Achievement achievement);
}
