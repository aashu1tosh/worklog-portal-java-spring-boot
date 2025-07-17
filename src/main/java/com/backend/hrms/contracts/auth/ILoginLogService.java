package com.backend.hrms.contracts.auth;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.hrms.dto.auth.LoginLogDTO;
import com.backend.hrms.entity.auth.LoginLogEntity;

public interface ILoginLogService {

    LoginLogEntity saveLoginLog(LoginLogDTO.Request loginLogRequestDto);

    LoginLogEntity updateLogoutTime(UUID id);

    Page<LoginLogEntity> get(Pageable pageable, UUID authId);

    void logoutFromOtherDevice(UUID id, UUID authId);

    LoginLogEntity isLoggedIn(UUID id);

    void asyncUpdateLogoutTime(UUID id);
}
