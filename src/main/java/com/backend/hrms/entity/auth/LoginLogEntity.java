package com.backend.hrms.entity.auth;

import com.backend.hrms.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginLogEntity extends BaseEntity {

    @Column(name = "login_time")
    private String loginTime;

    @Column(name = "logout_time", nullable = true)
    private String logOutTime;

    @Column(name = "device_id", nullable = true)
    private String deviceId;

    @Column(name = "client_ip", nullable = true)
    private String clientIp;

    @Column(name = "device_type", nullable = true)
    private String deviceType;

    @Column(name = "os", nullable = true)
    private String os;

    @Column(name = "browser", nullable = true)
    private String browser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;
}
