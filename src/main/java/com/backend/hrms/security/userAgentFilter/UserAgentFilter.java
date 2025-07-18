package com.backend.hrms.security.userAgentFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.utils.PropertyUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAgentFilter extends OncePerRequestFilter {

    private final String apiKey = PropertyUtil.getApiKey();

    private static final List<HttpMethod> RESTRICTED_METHODS = Arrays.asList(
            HttpMethod.POST,
            HttpMethod.PATCH,
            HttpMethod.DELETE,
            HttpMethod.PUT);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)

            throws ServletException, IOException {

        HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());

        if (RESTRICTED_METHODS.contains(requestMethod)) {
            String userAgent = request.getHeader("User-Agent");

            // If User-Agent is not present or doesn't contain "Mozilla" (case-insensitive)
            if (userAgent == null || !userAgent.toLowerCase().contains("mozilla")) {
                String xApiKey = request.getHeader("x-api-key");

                if (xApiKey == null || !xApiKey.equals(apiKey)) {
                    throw HttpException.forbidden("Forbidden: User browser");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}