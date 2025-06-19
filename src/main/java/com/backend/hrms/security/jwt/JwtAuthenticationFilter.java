package com.backend.hrms.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
            FilterChain chain) throws IOException, ServletException {

        String token = resolveToken(req); // <─ get it from header or cookie
        if (token != null) {
            try {
                Claims claims = jwt.parseAccessToken(token); // throws on bad/expired token
                String username = claims.getSubject();

                UserDetails user = users.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException ex) {
                throw HttpException.unauthorized("TOKEN_EXPIRED");
            }
        }
        chain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest req) {

        // 2️⃣ ‑ Cookie (HttpServletRequest#getCookies or WebUtils helper)
        Cookie jwtCookie = WebUtils.getCookie(req, "accessToken");
        return (jwtCookie != null) ? jwtCookie.getValue() : null;
    }
}
