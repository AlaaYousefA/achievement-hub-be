package com.achivhub.backend.persistence.collection;

import com.achivhub.backend.domain.model.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserCollection {

    SysUser getSysUserById(String id);

    Page<SysUser> getAllSysUsers(int page, int size, String sortBy, String sortDirection, List<String> department, List<String> kasitRole, String name, Long achievementsNumber);

    SysUser save(SysUser sysUser);

    SysUser findByUsername(String username);
}
