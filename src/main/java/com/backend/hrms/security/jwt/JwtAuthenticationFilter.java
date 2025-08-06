package com.backend.hrms.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    public JwtAuthenticationFilter(JwtService jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse res,
            @NonNull FilterChain chain) throws ServletException, IOException {

        try {
            String token = resolveAccessToken(req);

            if (token != null) {
                Claims claims = jwt.parseAccessToken(token);
                JwtPayload payload = JwtPayload.from(claims);
                String role = claims.get("role", String.class);

                List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role));

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        payload, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                var refreshToken = resolveRefreshToken(req);
                if (refreshToken != null) {
                    throw HttpException.unauthorized(Messages.TOKEN_EXPIRED);
                } else {
                    throw HttpException.unauthorized(Messages.UNAUTHORIZED);
                }
            }

            chain.doFilter(req, res);
        } catch (HttpException ex) {
            res.setStatus(ex.getStatus().value());
            res.setContentType("application/json");
            res.getWriter().write("""
                    {
                        "success": false,
                        "message": "%s",
                        "data": null
                    }
                    """.formatted(ex.getMessage()));
        } catch (JwtException ex) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("""
                    {
                        "success": false,
                        "message": "%s",
                        "data": null
                    }
                    """.formatted(Messages.TOKEN_EXPIRED));
        } catch (Exception ex) {
            // Catch-all for unexpected errors
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.setContentType("application/json");
            res.getWriter().write("""
                    {
                        "success": false,
                        "message": "An unexpected error occurred",
                        "data": null
                    }
                    """);
            ex.printStackTrace(); // Optional: log the full stack trace
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("~ JwtAuthenticationFilter ~ Request Path: " + path + ", Method: " + method);

        // Skip JWT authentication for public endpoints, error endpoints, webhook
        // endpoints, and OPTIONS requests
        return path.contains("/public") ||
                path.contains("/error") ||
                path.contains("/webhook") ||
                "OPTIONS".equalsIgnoreCase(method);
    }

    private String resolveAccessToken(HttpServletRequest req) {
        Cookie jwtCookie = WebUtils.getCookie(req, "accessToken");
        return (jwtCookie != null) ? jwtCookie.getValue() : null;
    }

    private String resolveRefreshToken(HttpServletRequest req) {
        Cookie jwtCookie = WebUtils.getCookie(req, "refreshToken");
        return (jwtCookie != null) ? jwtCookie.getValue() : null;
    }
}