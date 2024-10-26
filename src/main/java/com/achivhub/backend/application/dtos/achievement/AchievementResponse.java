package com.achivhub.backend.application.dtos.achievement;

import com.achivhub.backend.domain.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponse {
    private String id;

    private String userId;

    private StatusEnum status;

    private String comment;
}
