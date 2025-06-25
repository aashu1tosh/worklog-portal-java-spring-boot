package com.backend.hrms.constants.enums;

public enum Role {
    COMPANY_EMPLOYEE("ROLE_COMPANY_EMPLOYEE"),
    COMPANY_ADMIN("ROLE_COMPANY_ADMIN"),
    ADMIN("ROLE_ADMIN"),
    SUDO_ADMIN("ROLE_SUDO_ADMIN");

    private final String springSecurityRole;

    Role(String springSecurityRole) {
        this.springSecurityRole = springSecurityRole;
    }

    public String getSpringSecurityRole() {
        return springSecurityRole;
    }

    // Optional: if you need the raw enum name without the prefix for other logic
    public String getName() {
        return this.name();
    }
}