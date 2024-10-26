package com.achivhub.backend.domain.services;

import com.achivhub.backend.domain.enums.StatusEnum;
import com.achivhub.backend.domain.model.Achievement;
import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.domain.services.security.jwt.JwtService;
import com.achivhub.backend.persistence.collection.AchievementCollection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementsService {

    private final AchievementCollection achievementCollection;
    private final SysUsersService sysUsersService;
    private final JwtService jwtService;

    public Achievement createAchievement(HttpServletRequest header, Achievement achievement) {
        fillAchievementMissingInfo(achievement);
        connectUserWithAchievement(header, achievement);
        return achievementCollection.save(achievement);
    }

    public Achievement getAchievement(HttpServletRequest header, String id) {
        Achievement achievement = achievementCollection.findAchievementById(id);
        if (validateRequest(header, achievement)) {
            return achievement;
        }

        throw new IllegalStateException("user have no access to achievement with id = " + id);
    }

    public Page<Achievement> getAllAchievements(
            HttpServletRequest header,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String name,
            List<String> category,
            List<String> status,
            String time,
            String userIdf,
            String title
    ) {
        if (isAdmin(header)) {
            return achievementCollection.getAllAchievements(page, size, sortBy, sortDirection, name, category, status, time, userIdf, title);
        } else {
            String username = extractUsernameFromHeader(header);
            SysUser sysUser = sysUsersService.getSysUser(username);
            return achievementCollection.getAllAchievementsByUserId(sysUser.getId(), page, size, sortBy, sortDirection, name, category, status, time, userIdf, title);
        }
    }

    public Page<Achievement> getAllUserAchievements(
            HttpServletRequest header,
            String userId,
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String name,
            List<String> category,
            List<String> status,
            String time,
            String userIdf,
            String title
    ) {
        String username = extractUsernameFromHeader(header);
        SysUser sysUser = sysUsersService.getSysUser(username);
        if(!isAdmin(header) && !userId.equals(sysUser.getId())) {
            throw new IllegalStateException("Access Deniad");
        }

        return achievementCollection.getAllAchievementsByUserId(userId, page, size, sortBy, sortDirection, name, category, status, time, userIdf, title);
    }

    public Achievement updateAchievement(HttpServletRequest header, Achievement achievement, String id) {
        Achievement oldAchievement = achievementCollection.findAchievementById(id);

        if(validateRequest(header, oldAchievement)) {
            LocalDateTime oldTime = oldAchievement.getCreatedAt();
            String userId = oldAchievement.getUserId();

            BeanUtils.copyProperties(achievement, oldAchievement);

            oldAchievement.setId(id);
            oldAchievement.setUserId(userId);
            oldAchievement.setUpdatedAt(LocalDateTime.now());
            oldAchievement.setCreatedAt(oldTime);
            oldAchievement.setStatus(StatusEnum.PENDING);
            oldAchievement.setComment(null);

            return achievementCollection.save(oldAchievement);
        }

        throw new IllegalStateException("user have no access to achievement with id = " + id);
    }


    public Boolean deleteAchievement(HttpServletRequest header, String id) {
        Achievement achievement = achievementCollection.findAchievementById(id);
        if(validateRequest(header, achievement)) {
            return achievementCollection.deleteAchievementById(id);
        }

        throw new IllegalStateException("user have no access to achievement with id = " + id);
    }

    public Achievement approveAchievement(String id) {
        Achievement achievement = achievementCollection.findAchievementById(id);
        if (achievement.getStatus().equals(StatusEnum.APPROVED)) {
            throw new IllegalStateException("already approved");
        }

        achievement.setStatus(StatusEnum.APPROVED);
        return achievementCollection.save(achievement);
    }

    public Achievement rejectAchievement(String id, String message) {
        Achievement achievement = achievementCollection.findAchievementById(id);
        if (achievement.getStatus().equals(StatusEnum.REJECTED)) {
            throw new IllegalStateException("already rejected");
        }

        achievement.setStatus(StatusEnum.REJECTED);
        achievement.setComment(message);
        return achievementCollection.save(achievement);
    }


    private void connectUserWithAchievement(HttpServletRequest header, Achievement achievement) {
        String username = extractUsernameFromHeader(header);
        SysUser sysUser = sysUsersService.getSysUser(username);

        achievement.setUserId(sysUser.getId());
    }

    private void fillAchievementMissingInfo(Achievement achievement) {
        achievement.setStatus(StatusEnum.PENDING);
        achievement.setCreatedAt(LocalDateTime.now());
        achievement.setUpdatedAt(LocalDateTime.now());
        achievement.setId(UUID.randomUUID().toString());
    }

    private boolean validateRequest(HttpServletRequest header, Achievement achievement) {
        if (isAdmin(header)) {
            return true;
        }

        String username = extractUsernameFromHeader(header);
        SysUser sysUser = sysUsersService.getSysUser(username);
        return achievement.getUserId().equals(sysUser.getId());
    }

    private boolean isAdmin(HttpServletRequest header) {
        String username = extractUsernameFromHeader(header);
        SysUser sysUser = sysUsersService.getSysUser(username);
        return sysUser.getIdmRoles().contains("ADMIN") || sysUser.getIdmRoles().contains("SUB_ADMIN");
    }

    private String extractUsernameFromHeader(HttpServletRequest header) {
        String jwt = jwtService.extractToken(header);
        return jwtService.extractUsername(jwt);
    }
}
