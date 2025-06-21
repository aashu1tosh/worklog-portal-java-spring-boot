package com.backend.hrms.helpers.auth;

import jakarta.servlet.http.HttpServletRequest;

public class GetClientsIp {
    public static String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // If multiple IPs, take the first one
            return xForwardedFor.split(",")[0].trim();
        }

        // Fallback to remote address
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr != null ? remoteAddr : "unknown";
    }
}
