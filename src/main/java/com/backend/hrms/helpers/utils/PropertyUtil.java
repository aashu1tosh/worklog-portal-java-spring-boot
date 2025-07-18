package com.backend.hrms.helpers.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertyUtil {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null)
                props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBaseUrl() {
        return props.getProperty("app.base-url", "http://localhost:8000/api/v1");
    }

    public static List<String> getAllowedOrigins() {
        String origins = props.getProperty("app.allowed-origins", "http://localhost:5173");
        return Arrays.stream(origins.split(","))
                .map(String::trim) // remove any extra spaces
                .toList();
    }

}
