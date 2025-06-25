package com.backend.hrms.constants.enums;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    SUDO_ADMIN("ROLE_SUDO_ADMIN"),
    COMPANY_SUPER_ADMIN("ROLE_COMPANY_SUPER_ADMIN"),
    COMPANY_ADMIN("ROLE_COMPANY_ADMIN"),
    COMPANY_EMPLOYEE("ROLE_COMPANY_EMPLOYEE");

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