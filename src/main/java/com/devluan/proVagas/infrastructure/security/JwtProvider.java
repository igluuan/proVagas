package com.devluan.proVagas.infrastructure.security;

import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.devluan.proVagas.domain.user.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Getter
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final Long expiresIn = 3600L;

    public String generateToken(User user){
        var now = Instant.now();
        
        var scopes = user.getRoles()
        .stream()
        .map(role -> "ROLE_" + role.getName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("proVagas-service")
                .subject(user.getEmail())
                .claim("scope", scopes)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
}
