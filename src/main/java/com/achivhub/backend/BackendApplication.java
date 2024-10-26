package com.achivhub.backend;

import com.achivhub.backend.domain.enums.DepartmentEnum;
import com.achivhub.backend.domain.enums.KasitRoleEnum;
import com.achivhub.backend.persistence.database.SysUserMongoCollection;
import com.achivhub.backend.persistence.documents.SysUserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class BackendApplication {

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

//    @Bean
    public CommandLineRunner commandLineRunner(SysUserMongoCollection sysUserMongoCollection) {
         return args -> {
             SysUserDocument admin = SysUserDocument.builder()
                     .name("admin")
                     .email("admin@test.com")
                     .bio("just a test user am i?")
                     .department(DepartmentEnum.CIS)
                     .kasitRole(KasitRoleEnum.PROFESSOR)
                     .username("admin")
                     .password(passwordEncoder.encode("123"))
                     .idmRoles("ADMIN")
                     .location("JORDAN")
                     .build();

             SysUserDocument user1 = SysUserDocument.builder()
                     .name("user1")
                     .email("user1@test.com")
                     .bio("just a test user am i?")
                     .department(DepartmentEnum.CIS)
                     .kasitRole(KasitRoleEnum.BACHELOR_STUDENT)
                     .username("user1")
                     .password(passwordEncoder.encode("123"))
                     .idmRoles("USER")
                     .location("JORDAN")
                     .build();

             SysUserDocument user2 = SysUserDocument.builder()
                     .name("user2")
                     .email("user2@test.com")
                     .bio("just a test user am i?")
                     .department(DepartmentEnum.BIT)
                     .kasitRole(KasitRoleEnum.PROFESSOR)
                     .username("user2")
                     .password(passwordEncoder.encode("123"))
                     .idmRoles("USER")
                     .location("JORDAN")
                     .build();

             SysUserDocument user3 = SysUserDocument.builder()
                     .name("user3")
                     .email("user3@test.com")
                     .bio("just a test user am i?")
                     .department(DepartmentEnum.AI)
                     .kasitRole(KasitRoleEnum.BACHELOR_STUDENT)
                     .username("user3")
                     .password(passwordEncoder.encode("123"))
                     .idmRoles("USER")
                     .location("JORDAN")
                     .build();

             SysUserDocument user4 = SysUserDocument.builder()
                     .name("user4")
                     .email("user4@test.com")
                     .bio("just a test user am i?")
                     .department(DepartmentEnum.CIS)
                     .kasitRole(KasitRoleEnum.ASSISTANT_PROFESSOR)
                     .username("user4")
                     .password(passwordEncoder.encode("123"))
                     .idmRoles("USER")
                     .location("JORDAN")
                     .build();

             sysUserMongoCollection.save(admin);
             sysUserMongoCollection.save(user1);
             sysUserMongoCollection.save(user2);
             sysUserMongoCollection.save(user3);
             sysUserMongoCollection.save(user4);
         };
    }
}

