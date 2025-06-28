package com.backend.hrms.security.jwt;

import java.io.IOException;
import java.util.List;

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
import lombok.NonNull;

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
            @NonNull FilterChain chain) throws HttpException, IOException, ServletException {

        String token = resolveAccessToken(req);
        if (token != null) {
            try {
                Claims claims = jwt.parseAccessToken(token);

                /* ---- 1. Extract what you need from the token ------------------ */
                JwtPayload payload = JwtPayload.from(claims);
                String role = claims.get("role", String.class);

                /* ---- 2. Turn the role(s) into GrantedAuthority --------------- */
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(
                        role.startsWith("ROLE_") ? role : "ROLE_" + role));

                /* ---- 3. Build the Authentication object ---------------------- */
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        payload, // principal (user details)
                        null,
                        authorities); // authorities for @PreAuthorize, etc.

                /* ---- 4. Put it in the context -------------------------------- */
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException ex) {
                System.out.println("~ JwtAuthenticationFilter ~ JWT Exception: " + ex.getMessage());
                throw HttpException.unauthorized(Messages.TOKEN_EXPIRED);
            }
        } else {
            var refreshToken = resolveRefreshToken(req);
            if (refreshToken != null) {
                throw HttpException.unauthorized(Messages.TOKEN_EXPIRED);
            } else
                throw HttpException.unauthorized(Messages.UNAUTHORIZED);
        }
        chain.doFilter(req, res);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("~ JwtAuthenticationFilter ~ Request Path: " + path + ", Method: " + method);

        return path.contains("/public") || path.contains("/error") || "OPTIONS".equalsIgnoreCase(method);
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
