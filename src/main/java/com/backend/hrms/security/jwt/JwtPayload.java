package com.backend.hrms.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtPayload {
    private String id;
    private String role;

    // Constructor
    public JwtPayload(String id, String role) {
        this.id = id;
        this.role = role;
    }
}
