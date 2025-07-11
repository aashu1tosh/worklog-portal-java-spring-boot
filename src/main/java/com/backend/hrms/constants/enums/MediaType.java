// src/main/java/com/backend/hrms/constants/enums/MediaType.java
package com.backend.hrms.constants.enums;

import java.util.HashMap;
import java.util.Map;

import com.backend.hrms.exception.HttpException;

public enum MediaType {
    ProfilePicture("PROFILE_PICTURE"),
    // Add other media types as needed
    Document("DOCUMENT"),
    Image("IMAGE");

    private final String value;

    // A static map to quickly look up enum by its string value
    private static final Map<String, MediaType> BY_VALUE = new HashMap<>();

    static {
        for (MediaType mediaType : values()) {
            BY_VALUE.put(mediaType.value.toUpperCase(), mediaType); // Store uppercase for case-insensitive lookup
        }
    }

    MediaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // New static method to get enum from string value
    public static MediaType fromValue(String value) {
        MediaType mediaType = BY_VALUE.get(value.toUpperCase());
        if (mediaType == null) {
            throw HttpException.badRequest("Invalid Media Type value: " + value);
        }
        return mediaType;
    }
}