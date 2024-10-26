package com.achivhub.backend.domain.services.security.idm;

import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.persistence.collection.SysUserCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {
    private final SysUserCollection sysUserCollection;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserCollection.findByUsername(username);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(getRoles(user))
                .build();
    }

    private String[] getRoles(SysUser user) {
        if(user.getIdmRoles().isBlank())
            return new String[]{"USER"};

        return user.getIdmRoles().split(",");
    }
}
