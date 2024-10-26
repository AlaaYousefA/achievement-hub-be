package com.achivhub.backend.persistence.adapter;

import com.achivhub.backend.domain.mappers.SysUserMapper;
import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.persistence.collection.AchievementCollection;
import com.achivhub.backend.persistence.collection.SysUserCollection;
import com.achivhub.backend.persistence.database.SysUserMongoCollection;
import com.achivhub.backend.persistence.documents.SysUserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class SysUserAdapter implements SysUserCollection {

    private final SysUserMongoCollection sysUserMongoCollection;
    private final SysUserMapper sysUserMapper;
    private final MongoOperations mongoOperations;
    private final AchievementCollection achievementCollection;

    @Override
    public SysUser getSysUserById(String id) {
        if (sysUserMongoCollection.findById(id).isEmpty()) {
            throw new NoSuchElementException("no User found with id: " + id);
        }
        return sysUserMapper.documentToModel(sysUserMongoCollection.findById(id).get());
    }

    @Override
    public Page<SysUser> getAllSysUsers(int page, int size, String sortBy, String sortDirection, List<String> department, List<String> kasitRole, String name, Long achievementsNumber) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Criteria criteria = buildCriteria(department, kasitRole, name);
        Query query = new Query(criteria);

        List<SysUserDocument> documents = mongoOperations.find(query, SysUserDocument.class);

        List<SysUser> filteredUsers = documents.stream()
                .map(sysUserMapper::documentToModel)
                .filter(user -> {
                    if (achievementsNumber != null) {
                        long userAchievementsCount = achievementCollection.getAllAchievementsByUserId(
                                user.getId(),
                                0,
                                Integer.MAX_VALUE,
                                "time",
                                "asc",
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        ).getTotalElements();
                        return userAchievementsCount >= achievementsNumber;
                    }
                    return true;
                })
                .toList();

        int totalFiltered = (int) filteredUsers.size();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalFiltered);
        List<SysUser> pagedUsers = filteredUsers.subList(start, end);

        return new PageImpl<>(pagedUsers, pageable, totalFiltered);
    }


    @Override
    public SysUser save(SysUser sysUser) {
        SysUserDocument document = sysUserMapper.modelToDocument(sysUser);
        return sysUserMapper.documentToModel(sysUserMongoCollection.save(document));
    }

    @Override
    public SysUser findByUsername(String username) {
        if (sysUserMongoCollection.findByUsername(username).isEmpty()) {
            throw new NoSuchElementException("no User found with username: " + username);
        }
        return sysUserMapper.documentToModel(sysUserMongoCollection.findByUsername(username).get());
    }

    private Criteria buildCriteria(List<String> department, List<String> kasitRole, String name) {
        Criteria criteria = new Criteria();

        if (department != null && !department.isEmpty()) {
            criteria = criteria.and("department").in(department);
        }

        if (kasitRole != null && !kasitRole.isEmpty()) {
            criteria = criteria.and("kasitRole").in(kasitRole);
        }

        if (name != null && !name.isEmpty()) {
            criteria = criteria.and("name").regex(name, "i");
        }

        return criteria;
    }
}
