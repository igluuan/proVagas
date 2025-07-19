package com.devluan.proVagas.infrastructure.security;

import com.devluan.proVagas.application.service.user.TokenBlocklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final TokenBlocklistService tokenBlocklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Authorization header or invalid format");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        log.debug("Processing JWT token for request: {}", request.getRequestURI());

        if (tokenBlocklistService.isBlocklisted(token)) {
            log.warn("Token is blocklisted");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
            return;
        }

        try {
            final var jwt = jwtDecoder.decode(token);
            log.debug("JWT token decoded successfully for subject: {}", jwt.getSubject());
            final var user = new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, null);
            SecurityContextHolder.getContext().setAuthentication(user);
        } catch (JwtException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
