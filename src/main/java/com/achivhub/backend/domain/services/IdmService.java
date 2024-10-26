package com.achivhub.backend.domain.services;

import com.achivhub.backend.domain.model.SysAuthentication;
import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.domain.services.security.idm.SysUserDetailsService;
import com.achivhub.backend.domain.services.security.jwt.JwtService;
import com.achivhub.backend.domain.services.security.jwt.OtpService;
import com.achivhub.backend.persistence.collection.SysUserCollection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdmService {
    private final SysUserCollection sysUserCollection;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SysUserDetailsService sysUserDetailsService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    public SysAuthentication authenticate(SysAuthentication model) {
        Authentication authentication = getAuthenticationStatus(model);
        if (authentication.isAuthenticated()) {
            return buildSysAuthentication(model);
        }

        throw new UsernameNotFoundException("username or password incorrect");
    }

    private SysAuthentication buildSysAuthentication(SysAuthentication model) {
        UserDetails userDetails = sysUserDetailsService.loadUserByUsername(model.getUsername());
        String jwt = jwtService.generateToken(userDetails);
        model.setToken(jwt);
        return model;
    }

    private Authentication getAuthenticationStatus(SysAuthentication model) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword())
        );
    }

    public List<String> permissions(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        String username = jwtService.extractUsername(token);
        return Arrays.stream(sysUserCollection.findByUsername(username).getIdmRoles().split(",")).toList();
    }

    public Boolean validate(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = sysUserDetailsService.loadUserByUsername(authentication.getName());
        return jwtService.isTokenValid(token, userDetails);
    }

    public void forget(String username) {
        SysUser user = sysUserCollection.findByUsername(username);

        String otp = otpService.generateOtp();
        String salt = otpService.generateSalt();
        String hashedOtp = otpService.hashOtp(otp, salt);

        updateSysUser(user, hashedOtp, salt);

        String otpMessage = "your otp is :" + otp;
        log.info(otpMessage);
    }

    private void updateSysUser(SysUser user, String hashedOtp, String salt) {
        user.setOtp(hashedOtp);
        user.setOtpSalt(salt);
        user.setOtpTimestamp(LocalDateTime.now());
        sysUserCollection.save(user);
    }

    public Boolean verify(String username, String encodedOtp) {
        SysUser user = sysUserCollection.findByUsername(username);
        String otp = new String(Base64.getDecoder().decode(encodedOtp), StandardCharsets.UTF_8);

        if(!isOtpRequested(user)){
            throw new IllegalStateException("otp must be requested before verify");
        }

        if(isOtpExpired(user.getOtpTimestamp())){
            throw new IllegalStateException("otp expired");
        }

        String hashedInputOtp = otpService.hashOtp(otp, user.getOtpSalt());

        if(hashedInputOtp.equals(user.getOtp())){
            user.setIsOtpVerified(true);
            sysUserCollection.save(user);
            return true;
        }

        throw new IllegalStateException("otp is invalid");
    }

    private boolean isOtpExpired(LocalDateTime otpTimestamp) {
         return Duration.between(otpTimestamp, LocalDateTime.now()).toMinutes() > 3;
    }

    private boolean isOtpRequested(SysUser user) {
        return user.getOtp()!= null
                && user.getOtpTimestamp() != null
                && user.getOtpSalt() != null
                && !isOtpExpired(user.getOtpTimestamp());
    }

    public void change(String username, String password) {
        SysUser user = sysUserCollection.findByUsername(username);

        if(!isOtpValid(user)){
            throw  new IllegalStateException("there is no OTP");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setIsOtpVerified(true);
        sysUserCollection.save(user);
    }

    private boolean isOtpValid(SysUser user) {
        return user.getOtp()!= null
                && user.getOtpSalt() != null
                && user.getOtpTimestamp() != null
                && user.getIsOtpVerified() != null
                && !isOtpExpired(user.getOtpTimestamp())
                && user.getIsOtpVerified();
    }
}
