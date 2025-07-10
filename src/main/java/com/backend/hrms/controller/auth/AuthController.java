package com.backend.hrms.controller.auth;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.auth.LoginLogDTO;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.auth.LoginLogEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.helpers.auth.DeviceDetector;
import com.backend.hrms.helpers.auth.GetClientsIp;
import com.backend.hrms.helpers.utils.UUIDUtils;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.security.jwt.JwtService;
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

    public AuthController(AuthService authService, JwtService jwtService, LoginLogService loginLogService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.loginLogService = loginLogService;
    }

    @PostMapping("/public/login")
    public ApiResponse<AuthDTO.MeDTO> login(@Valid @RequestBody AuthDTO.LoginDTO body, HttpServletRequest request,
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

        Map<String, Object> claims = new HashMap<>();
        claims.put("key", log.getId().toString());
        claims.put("id", authEntity.getId());
        claims.put("role", authEntity.getRole());

        if (authEntity.getCompanyAdmin() != null && authEntity.getCompanyAdmin().getCompany() != null) {
            claims.put("companyId", authEntity.getCompanyAdmin().getCompany().getId().toString());
        }
        if (authEntity.getCompanyEmployee() != null && authEntity.getCompanyEmployee().getCompany() != null) {
            claims.put("companyId", authEntity.getCompanyEmployee().getCompany().getId().toString());
        }
        if (authEntity.getCompanyEmployee() != null) {
            claims.put("employeeId", authEntity.getCompanyEmployee().getId().toString());
        }
        String accessToken = jwtService.generateAccessToken(claims);

        String refreshToken = jwtService.generateRefreshToken(claims);

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

        var meData = authService.me(authEntity.getId());
        var responseData = AuthDTO.MeDTO.fromEntity(meData);

        return new ApiResponse<AuthDTO.MeDTO>(true, Messages.LOGIN_SUCCESS, responseData);
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
                UUID key = UUID.fromString(claims.get("key", String.class));
                loginLogService.isLoggedIn(key);

                // prepare claims for access token
                Map<String, Object> newClaims = new HashMap<>();
                newClaims.put("key", key.toString());
                newClaims.put("id", claims.get("id", String.class));
                newClaims.put("role", claims.get("role", String.class));

                // include companyId if it exists
                String companyId = claims.get("companyId", String.class);
                if (companyId != null) {
                    newClaims.put("companyId", companyId);
                }

                String employeeId = claims.get("employeeId", String.class);
                if (employeeId != null) {
                    newClaims.put("employeeId", employeeId);
                }

                // valid so generate new tokens
                String accessToken = jwtService.generateAccessToken(newClaims);

                String newRefreshToken = jwtService.generateRefreshToken(newClaims);

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

    @PatchMapping("/update-password")
    public ApiResponse<String> updatePassword(@Valid @RequestBody AuthDTO.UpdatePasswordDTO body,
            @AuthenticationPrincipal JwtPayload jwt, HttpServletResponse response) {

        authService.updatePassword(body, UUIDUtils.validateId(jwt.id()));
        loginLogService.updateLogoutTime(UUID.fromString(jwt.key()));
        loginLogService.asyncUpdateLogoutTime(UUID.fromString(jwt.id()));

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
        return new ApiResponse<>(true, "Password updated successfully", "");
    }

    private String resolveRefreshToken(HttpServletRequest req) {
        Cookie jwtCookie = WebUtils.getCookie(req, "refreshToken");
        return (jwtCookie != null) ? jwtCookie.getValue() : null;
    }
}
