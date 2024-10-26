package com.achivhub.backend.domain.model;

import com.achivhub.backend.domain.enums.DepartmentEnum;
import com.achivhub.backend.domain.enums.KasitRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {
    private String id;

    private String username;

    private String password;

    private byte[] avatar;

    private String name;

    private  String email;

    private DepartmentEnum department;

    private LocalDateTime joined;

    private String bio;

    private String location;

    private KasitRoleEnum kasitRole;

    private String idmRoles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String otp;

    private String otpSalt;

    private LocalDateTime otpTimestamp;

    private Boolean isOtpVerified;
}
