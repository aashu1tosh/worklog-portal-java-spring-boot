package com.backend.hrms.security.jwt;

import java.util.Map;

import io.jsonwebtoken.Claims;

public record JwtPayload(
        String id,
        String role,
        Map<String, Object> claims // keep the raw claims if you like
) {

    /** Build a JwtPayload from the JWT claims object. */
    public static JwtPayload from(Claims c) {
        // adapt the claim names to whatever you put in the token
        String id = c.get("id", String.class);
        String role = c.get("role", String.class);

        return new JwtPayload(id, role, c);
    }
}
