package com.achivhub.backend.domain.mappers;

import com.achivhub.backend.application.dtos.idm.SysAuthenticationRequest;
import com.achivhub.backend.application.dtos.idm.SysAuthenticationResponse;
import com.achivhub.backend.domain.model.SysAuthentication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysAuthenticationMapper {
    SysAuthentication requestToModel(SysAuthenticationRequest request);
    SysAuthenticationResponse modelToResponse(SysAuthentication model);
}
