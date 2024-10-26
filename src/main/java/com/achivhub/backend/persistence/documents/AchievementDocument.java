package com.achivhub.backend.persistence.documents;

import com.achivhub.backend.domain.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document("achievement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementDocument {
    private String id;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "image")
    private Object image;

    @Field(name = "location")
    private String location;

    @Field(name = "comment")
    private String comment;

    @Field(name = "title")
    private String title;

    @Field(name = "time")
    private LocalDateTime time;

    @Field(name = "category")
    private String category;

    @Field(name = "reference")
    private Map<String,Object> reference;

    @Field(name = "tags")
    private String[] tags;

    @Field(name = "status")
    private StatusEnum status;


    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;
}
