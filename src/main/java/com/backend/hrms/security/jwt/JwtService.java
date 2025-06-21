package com.backend.hrms.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtProperties props;

    JwtService(JwtProperties props) {
        this.props = props;
    }

    public String generateAccessToken(Map<String, ?> claims) {
        return generateToken(claims, props.getAccess());
    }

    public String generateRefreshToken(Map<String, ?> claims) {
        return generateToken(claims, props.getRefresh());
    }

    public Claims parseAccessToken(String token) {
        return parse(token, props.getAccess());
    }

    public Claims parseRefreshToken(String token) {
        return parse(token, props.getRefresh());
    }

    private String generateToken(
            Map<String, ?> claims,
            JwtProperties.Token cfg) {
        SecretKey key = Keys.hmacShaKeyFor(cfg.getSecret().getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(new HashMap<>(claims))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(cfg.getExpire())))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parse(String token, JwtProperties.Token cfg) {
        SecretKey key = Keys.hmacShaKeyFor(cfg.getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
