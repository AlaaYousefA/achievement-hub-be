package com.achivhub.backend.application.dtos.idm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAuthenticationRequest {
    private String username;
    private String password;
}
