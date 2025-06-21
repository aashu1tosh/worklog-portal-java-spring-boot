package com.backend.hrms.dto.auth;

import java.util.UUID;

import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.auth.LoginLogEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class LoginLogDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Request {
        private String clientIp;
        private String deviceType;
        private String os;
        private String browser;
        private String deviceId;
        private UUID authId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Response extends BaseResponse {
        private String loginTime;
        private String logOutTime;
        private String clientIp;
        private String deviceType;
        private String os;
        private String browser;
        private String deviceId;
        private UUID authId;

        public static Response fromEntity(LoginLogEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .loginTime(entity.getLoginTime())
                    .logOutTime(entity.getLogOutTime() != null ? entity.getLogOutTime() : null)
                    .clientIp(entity.getClientIp())
                    .deviceId(entity.getDeviceId())
                    .deviceType(entity.getDeviceType())
                    .os(entity.getOs())
                    .browser(entity.getBrowser())
                    .authId(entity.getAuth() != null ? entity.getAuth().getId() : null)
                    .build();
        }
    }
}
