package com.backend.hrms.security.jwt;

import java.util.Map;

import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.Nullable;

public record JwtPayload(
        String key,
        String id,
        String role,
        @Nullable String companyId,
        Map<String, Object> claims) {

    /** Build a JwtPayload from the JWT claims object. */
    public static JwtPayload from(Claims c) {
        // adapt the claim names to whatever you put in the token
        String id = c.get("id", String.class);
        String role = c.get("role", String.class);
        String key = c.get("key", String.class);
        String companyId = c.get("companyId", String.class);

        return new JwtPayload(key, id, role, companyId, c);
    }
}
