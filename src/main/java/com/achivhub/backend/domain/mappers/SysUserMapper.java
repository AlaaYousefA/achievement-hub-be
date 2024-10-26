package com.achivhub.backend.domain.mappers;

import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.persistence.documents.SysUserDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AchievementMapper.class)
public interface SysUserMapper {
    SysUser documentToModel(SysUserDocument document);
    SysUserDocument modelToDocument(SysUser model);
}
