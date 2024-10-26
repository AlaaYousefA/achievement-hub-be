package com.achivhub.backend.application.controllers;

import com.achivhub.backend.application.dtos.idm.SysAuthenticationRequest;
import com.achivhub.backend.application.dtos.idm.SysAuthenticationResponse;
import com.achivhub.backend.domain.mappers.SysAuthenticationMapper;
import com.achivhub.backend.domain.model.SysAuthentication;
import com.achivhub.backend.domain.services.IdmService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/idm")
@RequiredArgsConstructor
@Slf4j
public class IdmController {

    private final IdmService idmService;
    private final SysAuthenticationMapper sysAuthenticationMapper;

    @PostMapping("/authenticate")
    public ResponseEntity<SysAuthenticationResponse> authenticate(@RequestBody SysAuthenticationRequest payload) {
        SysAuthentication model = sysAuthenticationMapper.requestToModel(payload);
        return ResponseEntity.ok(sysAuthenticationMapper.modelToResponse(idmService.authenticate(model)));
    }


    @GetMapping("/permissions")
    public ResponseEntity<List<String>> permissions(HttpServletRequest request) {
        return ResponseEntity.ok(idmService.permissions(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(HttpServletRequest request) {
        return ResponseEntity.ok(idmService.validate(request));
    }

    @GetMapping("/forget")
    @ResponseStatus(HttpStatus.OK)
    public void forget(@RequestParam("username") String username) {
        idmService.forget(username);
    }


    @GetMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestParam("username") String username, @RequestParam("otp") String otp) {
        return ResponseEntity.ok(idmService.verify(username, otp));
    }

    @GetMapping("/change")
    @ResponseStatus(HttpStatus.OK)
    public void change(@RequestParam("username") String username, @RequestParam("password") String password) {
        idmService.change(username, password);
    }

}

