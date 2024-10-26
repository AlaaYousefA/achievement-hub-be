package com.achivhub.backend.application.dtos.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRequest {
    private String name;

    private String description;

    private Object image;

    private String location;

    private String title;

    private LocalDateTime time;

    private String category;

    private Map<String,Object> reference;

    private String[] tags;
}
