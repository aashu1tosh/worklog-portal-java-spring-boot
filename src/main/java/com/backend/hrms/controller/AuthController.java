package com.backend.hrms.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.AuthDTO;
import com.backend.hrms.dto.ApiResponse.ApiResponse;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${env-name:DEVELOPMENT}")
    private String envName;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/public/login")
    public ApiResponse<String> login(@Valid @RequestBody AuthDTO.LoginDTO body, HttpServletResponse response) {
        Map<String, String> tokens = authService.login(body);

        boolean cookieSecure = !"DEVELOPMENT".equalsIgnoreCase(envName);
        ResponseCookie accessCookie = ResponseCookie
                .from("accessToken", tokens.get("access"))
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/") // cookie is valid for entire domain
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", tokens.get("refresh"))
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        // 3) Add Set‑Cookie headers
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new ApiResponse<String>(true, Messages.LOGIN_SUCCESS, "");
    }

    @PostMapping("/public/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register() {
        System.out.println("Register endpoint hit");
        return new ApiResponse<String>(true, "Register endpoint is not implemented yet.", "");
    }
}
