package com.achivhub.backend.domain.model;

import com.achivhub.backend.domain.enums.StatusEnum;
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
public class Achievement {
    private String id;

    private String userId;

    private String name;

    private String description;

    private Object image;

    private String location;

    private String comment;

    private String title;

    private LocalDateTime time;

    private String category;

    private Map<String,Object> reference;

    private String[] tags;

    private StatusEnum status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
