package com.backend.hrms.entity;

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

    @Column(name="logout_time", nullable = true)
    private String logOutTime;

    @Column(name = "device_id")
    private String deviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;
}
