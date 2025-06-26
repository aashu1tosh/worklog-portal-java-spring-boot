package com.backend.hrms.controller.auth;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.auth.LoginLogDTO;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.auth.LoginLogEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.helpers.auth.DeviceDetector;
import com.backend.hrms.helpers.auth.GetClientsIp;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.security.jwt.JwtService;
import com.backend.hrms.service.AdminService;
import com.backend.hrms.service.auth.AuthService;
import com.backend.hrms.service.auth.LoginLogService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
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
    private final LoginLogService loginLogService;
    private final AdminService adminService;

    public AuthController(AuthService authService, JwtService jwtService, LoginLogService loginLogService,
            AdminService adminService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.loginLogService = loginLogService;
        this.adminService = adminService;
    }

    @PostMapping("/public/login")
    public ApiResponse<String> login(@Valid @RequestBody AuthDTO.LoginDTO body, HttpServletRequest request,
            HttpServletResponse response) {
        AuthEntity authEntity = authService.login(body);

        if (authEntity == null)
            throw HttpException.badRequest(Messages.INVALID_CREDENTIALS);

        String deviceId = null;
        // Try to get deviceId from cookies
        if (request.getCookies() != null) {
            deviceId = Arrays.stream(request.getCookies())
                    .filter(cookie -> "deviceId".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        // If not found, generate a new UUID
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString();
        }

        DeviceDetector.DeviceInfo deviceInfo = DeviceDetector.detectDevice(request);
        String clientIp = GetClientsIp.getClientIp(request);

        LoginLogDTO.Request loginLogRequestDto = LoginLogDTO.Request.builder()
                .clientIp(clientIp)
                .deviceType(deviceInfo.getDeviceType().toString())
                .os(deviceInfo.getOs())
                .browser(deviceInfo.getBrowser())
                .deviceId(deviceId)
                .authId(authEntity.getId())
                .build();

        LoginLogEntity log = loginLogService.saveLoginLog(loginLogRequestDto);

        String accessToken = jwtService.generateAccessToken(Map.of(
                "key", log.getId().toString(),
                "id", authEntity.getId(),
                "role", authEntity.getRole()));

        String refreshToken = jwtService.generateRefreshToken(Map.of(
                "key", log.getId().toString(),
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

        ResponseCookie deviceIdCookie = ResponseCookie
                .from("deviceId", deviceId)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(3650)) // 10 years
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deviceIdCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new ApiResponse<String>(true, Messages.LOGIN_SUCCESS, "");
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasAnyRole('SUDO_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register(@Valid @RequestBody AuthDTO.RegisterAdminDTO body) {

        if (Role.SUDO_ADMIN.equals(body.getRole()))
            throw HttpException.forbidden("Invalid Role");
        adminService.register(body);
        return new ApiResponse<String>(true, "Register endpoint is not implemented yet.", "");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@AuthenticationPrincipal JwtPayload jwt, HttpServletResponse response) {

        UUID userKey = null;

        if (jwt != null) {
            userKey = UUID.fromString(jwt.key());
        } else
            throw HttpException.unauthorized(Messages.UNAUTHORIZED);

        // Here you would typically invalidate the session or token
        loginLogService.updateLogoutTime(userKey);

        // String token = resolveRefreshToken(request);
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

    @GetMapping("/is-authenticated")
    public ApiResponse<AuthDTO.MeDTO> isAuthenticated(
            @AuthenticationPrincipal JwtPayload jwt) {
        loginLogService.isLoggedIn(UUID.fromString(jwt.key()));
        AuthEntity authEntity = authService.me(UUID.fromString(jwt.id()));

        AuthDTO.MeDTO response = AuthDTO.MeDTO.fromEntity(authEntity);

        return new ApiResponse<AuthDTO.MeDTO>(true, Messages.SUCCESS, response);
    }

    @PostMapping("/public/refresh-token")
    public ApiResponse<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = resolveRefreshToken(request);

        if (refreshToken != null) {
            try {

                Claims claims = jwtService.parseRefreshToken(refreshToken);
                UUID key = claims.get("key", UUID.class);
                loginLogService.isLoggedIn(key);

                // valid so generate new tokens
                String accessToken = jwtService.generateAccessToken(Map.of(
                        "key", key.toString(),
                        "id", claims.get("id", UUID.class),
                        "role", claims.get("role", String.class)));

                String newRefreshToken = jwtService.generateRefreshToken(Map.of(
                        "key", key.toString(),
                        "id", claims.get("id", UUID.class),
                        "role", claims.get("role", String.class)));

                boolean cookieSecure = !"DEVELOPMENT".equalsIgnoreCase(envName);

                ResponseCookie accessCookie = ResponseCookie
                        .from("accessToken", accessToken)
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ofDays(7))
                        .build();

                ResponseCookie refreshCookie = ResponseCookie
                        .from("refreshToken", newRefreshToken)
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(Duration.ofDays(7))
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            } catch (Exception e) {
                throw HttpException.badRequest(Messages.TOKEN_REFRESH_FAILED);
            }
        } else
            throw HttpException.badRequest(Messages.TOKEN_REFRESH_FAILED);

        return new ApiResponse<String>(true, Messages.TOKEN_REFRESH, "");
    }

    private String resolveRefreshToken(HttpServletRequest req) {
        Cookie jwtCookie = WebUtils.getCookie(req, "refreshToken");
        return (jwtCookie != null) ? jwtCookie.getValue() : null;
    }
}
