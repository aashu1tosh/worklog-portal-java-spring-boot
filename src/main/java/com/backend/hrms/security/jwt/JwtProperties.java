package com.backend.hrms.security.jwt;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter @Setter
public class JwtProperties {
    private Token access;
    private Token refresh;

    @Getter @Setter
    public static class Token {
        private String secret;
        private Duration expire;
    }
}
