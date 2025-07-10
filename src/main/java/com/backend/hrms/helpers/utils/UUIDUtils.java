package com.backend.hrms.helpers.utils;

import java.util.UUID;

import com.backend.hrms.exception.HttpException;

public class UUIDUtils {

    public static UUID validateId(String idStr) {
        if (idStr == null || idStr.isBlank()) {
            throw HttpException.badRequest("Invalid id format");
        }

        try {
            return UUID.fromString(idStr);
        } catch (IllegalArgumentException ex) {
            throw HttpException.badRequest("Invalid id format");
        }
    }
}
