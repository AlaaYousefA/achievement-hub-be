package com.achivhub.backend.application.controllers;

import com.achivhub.backend.application.dtos.achievement.AchievementRequest;
import com.achivhub.backend.application.dtos.achievement.AchievementResponse;
import com.achivhub.backend.application.dtos.rejection.RejectionRequest;
import com.achivhub.backend.domain.mappers.AchievementMapper;
import com.achivhub.backend.domain.model.Achievement;
import com.achivhub.backend.domain.services.AchievementsService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/achievements")
public class AchievementsController {
    private final AchievementsService achievementsService;
    private final AchievementMapper achievementMapper;

    @PostMapping
    public ResponseEntity<AchievementResponse> createAchievement(
            HttpServletRequest header,
            @RequestBody AchievementRequest payload
    ) {
        Achievement model = achievementMapper.requestToModel(payload);
        return ResponseEntity.ok(
                achievementMapper.modelToResponse(
                        achievementsService.createAchievement(header, model)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievement(
            HttpServletRequest header,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(achievementsService.getAchievement(header, id));
    }

    @GetMapping
    public ResponseEntity<Page<Achievement>> getAllAchievements(
            HttpServletRequest header,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) List<String> category,
            @RequestParam(name = "status", required = false) List<String> status,
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "userIdf", required = false) String userIdf,
            @RequestParam(name = "title", required = false) String title
    ) {
        return ResponseEntity.ok(achievementsService.getAllAchievements(header, page, size, sortBy, sortDirection, name, category, status, time, userIdf, title));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<Achievement>> getAllUserAchievements(
            HttpServletRequest header,
            @RequestParam(name = "userId") String userId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) List<String> category,
            @RequestParam(name = "status", required = false) List<String> status,
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "userIdf", required = false) String userIdf,
            @RequestParam(name = "title", required = false) String title
    ) {
        return ResponseEntity.ok(achievementsService.getAllUserAchievements(header, userId, page, size, sortBy, sortDirection, name, category, status, time, userIdf, title));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponse> updateAchievement(
            HttpServletRequest header,
            @RequestBody AchievementRequest payload,
            @PathVariable("id") String id
    ) {
        Achievement model = achievementMapper.requestToModel(payload);
        return ResponseEntity.ok(achievementMapper.modelToResponse(achievementsService.updateAchievement(header, model, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAchievement(
            HttpServletRequest header,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(achievementsService.deleteAchievement(header, id));
    }

    @PutMapping("/{id}/approve")
    @RolesAllowed({"ADMIN", "SUB_ADMIN"})
    public ResponseEntity<AchievementResponse> approveAchievement(@PathVariable("id") String id) {
        return ResponseEntity.ok(achievementMapper.modelToResponse(achievementsService.approveAchievement(id)));
    }

    @PutMapping("/{id}/reject")
    @RolesAllowed({"ADMIN", "SUB_ADMIN"})
    public ResponseEntity<AchievementResponse> rejectAchievement(@PathVariable("id") String id, @RequestBody RejectionRequest payload) {
        return ResponseEntity.ok(achievementMapper.modelToResponse(achievementsService.rejectAchievement(id, payload.getMessage())));
    }
}
