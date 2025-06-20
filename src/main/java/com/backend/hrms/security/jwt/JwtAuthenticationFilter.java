package com.backend.hrms.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.backend.hrms.exception.HttpException;

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
    private final UserDetailsService users;

    public JwtAuthenticationFilter(JwtService jwt, UserDetailsService users) {
        this.jwt = jwt;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException, HttpException {

        String token = resolveAccessToken(req); // <─ get it from header or cookie
        if (token != null) {
            try {
                Claims claims = jwt.parseAccessToken(token);
                System.out.println(claims + "check this");
                String username = claims.get("id", String.class);
                System.out.println("~ JwtAuthenticationFilter ~ username:" + username);

                /* ---- 1. Extract what you need from the token ------------------ */
                String userId = claims.get("id", String.class); // your own “principal”
                String role = claims.get("role", String.class); // e.g. "ADMIN"

                /* ---- 2. Turn the role(s) into GrantedAuthority --------------- */
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(
                        role.startsWith("ROLE_") ? role : "ROLE_" + role));

                /* ---- 3. Build the Authentication object ---------------------- */
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userId, // principal (you decide what to store here)
                        null, // credentials (none for JWT)
                        authorities); // authorities for @PreAuthorize, etc.

                /* ---- 4. Put it in the context -------------------------------- */
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException ex) {
                System.out.println("~ JwtAuthenticationFilter ~ JWT Exception: " + ex.getMessage());
                throw HttpException.unauthorized("TOKEN_EXPIRED");
            }
        }
        chain.doFilter(req, res);
    }

    private String resolveAccessToken(HttpServletRequest req) {
        Cookie jwtCookie = WebUtils.getCookie(req, "accessToken");
        return (jwtCookie != null) ? jwtCookie.getValue() : null;
    }
}
