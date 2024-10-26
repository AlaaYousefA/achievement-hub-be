package com.achivhub.backend.configurations.security.jwt;

import com.achivhub.backend.domain.services.security.idm.SysUserDetailsService;
import com.achivhub.backend.domain.services.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${achievementhub.jwt.authHeader.key}")
    private String authHeaderKey;

    private final JwtService jwtService;
    private final SysUserDetailsService sysUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(authHeaderKey);
        if (!isValidJwtHeader(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(authorizationHeader);
        String username = extractUsernameFromJwt(jwt);

        if (shouldAuthenticate(username)) {
            UserDetails user = loadUserDetails(username);
            if (isAuthenticationValid(user, jwt)) {
                UsernamePasswordAuthenticationToken token = getAuthenticationToken(username, user);
                setAuthenticationDetails(request, token);
                setSecurityContextHolder(token);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContextHolder(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private void setAuthenticationDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken token) {
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String username, UserDetails user) {
        return new UsernamePasswordAuthenticationToken(
                username,
                user.getPassword(),
                user.getAuthorities()
        );
    }

    private UserDetails loadUserDetails(String username) {
        return sysUserDetailsService.loadUserByUsername(username);
    }

    private boolean isAuthenticationValid(UserDetails user, String jwt) {
        return user != null && jwtService.isTokenValid(jwt, user);
    }

    private Boolean shouldAuthenticate(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private String extractUsernameFromJwt(String jwt) {
        return jwtService.extractUsername(jwt);
    }

    private String extractJwtFromHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private Boolean isValidJwtHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}
