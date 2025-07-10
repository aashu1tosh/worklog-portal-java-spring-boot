package com.backend.hrms.service.auth;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.auth.LoginLogDTO;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.auth.LoginLogEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.auth.AuthRepository;
import com.backend.hrms.repository.auth.LoginLogRepository;

@Service
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;
    private final AuthRepository authRepository; // Keep AuthRepository as a dependency

    public LoginLogService(LoginLogRepository loginLogRepository, AuthRepository authRepository) {
        this.loginLogRepository = loginLogRepository;
        this.authRepository = authRepository;
    }

    public LoginLogEntity saveLoginLog(LoginLogDTO.Request loginLogRequestDto) {
        AuthEntity authEntity = authRepository.findById(loginLogRequestDto.getAuthId())
                .orElseThrow(
                        () -> HttpException
                                .badRequest("User not found" + loginLogRequestDto.getAuthId()));

        LoginLogEntity loginLogEntity = LoginLogEntity.builder()
                .loginTime(Instant.now().toString())
                .clientIp(loginLogRequestDto.getClientIp())
                .deviceType(loginLogRequestDto.getDeviceType())
                .os(loginLogRequestDto.getOs())
                .browser(loginLogRequestDto.getBrowser())
                .deviceId(loginLogRequestDto.getDeviceId())
                .auth(authEntity)
                .build();

        return loginLogRepository.save(loginLogEntity);
    }

    public LoginLogEntity updateLogoutTime(UUID id) {
        LoginLogEntity loginLogEntity = loginLogRepository.findById(id)
                .orElseThrow(() -> HttpException.badRequest("LoginLogEntity not for this user"));

        loginLogEntity.setLogOutTime(Instant.now().toString());
        return loginLogRepository.save(loginLogEntity);
    }

    public Page<LoginLogEntity> get(Pageable pageable, UUID authId) {
        return loginLogRepository.findAllByAuthId(authId, pageable);
    }

    public void logoutFromOtherDevice(UUID id, UUID authId) {
        LoginLogEntity loginLogEntity = loginLogRepository.findByIdAndAuthId(id, authId)
                .orElseThrow(() -> HttpException.notFound("Login log not found"));

        if (loginLogEntity.getLogOutTime() != null) {
            throw HttpException.badRequest("Already logged out from this device");
        }

        loginLogEntity.setLogOutTime(Instant.now().toString());
        loginLogRepository.save(loginLogEntity);
    }

    public LoginLogEntity isLoggedIn(UUID id) {
        var data = loginLogRepository.findById(id)
                .orElseThrow(() -> HttpException.notFound("Not Authenticated. Login Again!"));

        if (data.getLogOutTime() != null)
            throw HttpException.unauthorized("Already logged out from this device");

        return data;
    }

    @Async
    public void asyncUpdateLogoutTime(UUID id) {
        try {
            var sessions = loginLogRepository.findLoggedInSessionsByAuthId(id);

            for (LoginLogEntity session : sessions) {
                session.setLogOutTime(Instant.now().toString());
            }

            // 3. Save all updated sessions
            loginLogRepository.saveAll(sessions);
        } catch (Exception e) {
            // Handle exception if needed
            System.err.println("Error updating logout time: " + e.getMessage());
        }
    }
}