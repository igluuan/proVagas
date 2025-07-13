package com.devluan.proVagas.infrastructure.logging;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggerConfig {

    @Bean
    @Scope("prototype")
    public LoggerService logger(InjectionPoint injectionPoint) {
        return new CustomLoggerImpl(injectionPoint.getMember().getDeclaringClass());
    }
}
