package com.achivhub.backend.domain.services;

import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.persistence.collection.SysUserCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class SysUsersService {
    private final SysUserCollection sysUserCollection;

    public SysUser getSysUser(String id) {
        try {
            return sysUserCollection.getSysUserById(id);
        } catch (NoSuchElementException e) {
            return sysUserCollection.findByUsername(id);
        }
    }

    public Page<SysUser> getAllSysUsers(int page, int size, String sortBy, String sortDirection, List<String> department, List<String> kasitRole, String name, Long achievementsNumber) {
        return sysUserCollection.getAllSysUsers(page, size, sortBy, sortDirection, department, kasitRole, name, achievementsNumber);
    }

    public SysUser save(SysUser sysUser) {
        return sysUserCollection.save(sysUser);
    }

    public Boolean upgradeIdmRoles(String username, String roles) {
        SysUser sysUser = sysUserCollection.findByUsername(username);
        sysUser.setIdmRoles(roles);
        sysUser.setUpdatedAt(LocalDateTime.now());
        sysUserCollection.save(sysUser);
        return true;
    }
}
