package com.devluan.proVagas.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
public class JwtProperties {
    private String publicKey;
    private String privateKey;
}
