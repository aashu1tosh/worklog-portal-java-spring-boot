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
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.entity.AuthEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtService;
import com.backend.hrms.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${env-name:DEVELOPMENT}")
    private String envName;

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;

    }

    @PostMapping("/public/login")
    public ApiResponse<String> login(@Valid @RequestBody AuthDTO.LoginDTO body, HttpServletResponse response) {
        AuthEntity authEntity = authService.login(body);

        if (authEntity == null) {
            throw HttpException.badRequest(Messages.INVALID_CREDENTIALS);
        }

        // Assuming authService.login() returns an AuthEntity with the necessary details
        String accessToken = jwtService.generateAccessToken(Map.of(
                "id", authEntity.getId(),
                "role", authEntity.getRole()));

        String refreshToken = jwtService.generateRefreshToken(Map.of(
                "id", authEntity.getId(),
                "role", authEntity.getRole()));

        boolean cookieSecure = !"DEVELOPMENT".equalsIgnoreCase(envName);
        ResponseCookie accessCookie = ResponseCookie
                .from("accessToken", accessToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/") // cookie is valid for entire domain
                .maxAge(Duration.ofDays(7))
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

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

    @PostMapping("/public/logout")
    public ApiResponse<String> logout(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie
                .from("accessToken", "")
                .httpOnly(true)
                .secure(!"DEVELOPMENT".equalsIgnoreCase(envName))
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(!"DEVELOPMENT".equalsIgnoreCase(envName))
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new ApiResponse<String>(true, Messages.LOGOUT_SUCCESS, "");
    }

    @PostMapping("/is-authenticated")
    public ApiResponse<Boolean> isAuthenticated() {
        // This endpoint can be used to check if the user is authenticated
        // The actual authentication logic would typically be handled by Spring Security
        return new ApiResponse<Boolean>(true, Messages.SUCCESS, true);
    }

    @PostMapping("/public/refresh-token")
    public ApiResponse<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        System.out.println("Refresh Token: " + refreshToken);

        if (refreshToken == null || refreshToken.isEmpty())
            throw HttpException.badRequest(Messages.TOKEN_REFRESH_FAILED);

        // Map<String, String> tokens = authService.refreshToken();
        return new ApiResponse<String>(true, Messages.TOKEN_REFRESH, "");
    }
}
