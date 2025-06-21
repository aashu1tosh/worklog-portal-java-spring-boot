package com.backend.hrms.service.auth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
                .loginTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .clientIp(loginLogRequestDto.getClientIp())
                .deviceType(loginLogRequestDto.getDeviceType())
                .os(loginLogRequestDto.getOs())
                .browser(loginLogRequestDto.getBrowser())
                .deviceId(loginLogRequestDto.getDeviceId())
                .auth(authEntity)
                .build();

        return loginLogRepository.save(loginLogEntity);
    }

    // You might also need a way to find the most recent login log for a user
    // to update their logout time. This requires a custom method in
    // LoginLogRepository.
    public LoginLogEntity updateLogoutTime(UUID authId) {
        // To find the *specific* login log to update for logout,
        // you might need a more sophisticated query than just findById(UUID id).
        // For example, finding the most recent login log for a given authId that
        // doesn't have a logout time yet.
        // Let's assume for simplicity, you're passing the ID of the LoginLogEntity
        // itself to update.
        // If you intend to pass the authId of the user, you'd need a custom repository
        // method like:
        // Optional<LoginLogEntity>
        // findTopByAuthIdAndLogOutTimeIsNullOrderByLoginTimeDesc(UUID authId);
        // And then:
        // LoginLogEntity loginLogEntity =
        // loginLogRepository.findTopByAuthIdAndLogOutTimeIsNullOrderByLoginTimeDesc(authId)
        // .orElseThrow(() -> new RuntimeException("No active login log found for
        // authId: " + authId));

        // For now, let's assume `authId` here refers to the primary key (UUID) of the
        // LoginLogEntity
        // that you want to mark as logged out. If it's the user's authId, you need a
        // custom query as above.
        LoginLogEntity loginLogEntity = loginLogRepository.findById(authId) // This finds by the primary key of
                                                                            // LoginLogEntity
                .orElseThrow(() -> new RuntimeException("LoginLogEntity not found for ID: " + authId));

        loginLogEntity.setLogOutTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return loginLogRepository.save(loginLogEntity);
    }
}