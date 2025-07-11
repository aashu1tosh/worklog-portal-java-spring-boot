package com.backend.hrms.constants.enums;

public enum MediaType {
    ProfilePicture("PROFILE_PICTURE");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
