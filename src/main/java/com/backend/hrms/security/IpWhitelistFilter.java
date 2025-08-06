package com.backend.hrms.security;

import java.io.IOException;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.hrms.helpers.utils.PropertyUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IpWhitelistFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_IPS = PropertyUtil.getAllowedIps();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String clientIp = getClientIpAddress(request);

        if (!isIpAllowed(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                        "success": false,
                        "message": "Access denied",
                        "data": null
                    }
                    """.formatted(clientIp));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Skip IP filtering for public endpoints, error endpoints, and OPTIONS requests
        return path.contains("/public") ||
                path.contains("/error") ||
                "OPTIONS".equalsIgnoreCase(method);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs, take the first one
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        String xForwardedProto = request.getHeader("X-Forwarded-Proto");
        if (xForwardedProto != null) {
            String remoteAddr = request.getRemoteAddr();
            if (remoteAddr != null) {
                return remoteAddr;
            }
        }

        return request.getRemoteAddr();
    }

    private boolean isIpAllowed(String clientIp) {
        return ALLOWED_IPS.contains(clientIp) ||
                clientIp.equals("0:0:0:0:0:0:0:1") || // IPv6 localhost
                isLocalhost(clientIp);
    }

    private boolean isLocalhost(String ip) {
        return "127.0.0.1".equals(ip) ||
                "::1".equals(ip) ||
                "0:0:0:0:0:0:0:1".equals(ip) ||
                "localhost".equals(ip);
    }
}