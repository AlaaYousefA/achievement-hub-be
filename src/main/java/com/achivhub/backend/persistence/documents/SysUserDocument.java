package com.achivhub.backend.persistence.documents;

import com.achivhub.backend.domain.enums.DepartmentEnum;
import com.achivhub.backend.domain.enums.KasitRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUserDocument {
    private String id;

    @Indexed(unique = true)
    @Field(name = "username")
    private String username;

    @Field(name = "password")
    private String password;

    @Field(name = "avatar")
    private byte[] avatar;

    @Field(name = "name")
    private String name;

    @Field(name = "email")
    private  String email;

    @Field(name = "department")
    private DepartmentEnum department;

    @Field(name = "joined")
    private LocalDateTime joined;

    @Field(name = "bio")
    private String bio;

    @Field(name = "location")
    private String location;

    @Field(name = "user_role")
    private KasitRoleEnum kasitRole;

    @Field(name = "idm_roles")
    private String idmRoles;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    @Field(name = "otp")
    private String otp;

    @Field(name = "otp_salt")
    private String otpSalt;

    @Field(name = "otp_timestamp")
    private LocalDateTime otpTimestamp;

    @Field(name = "is_otp_verified")
    private Boolean isOtpVerified;
}
