package com.backend.hrms.entity.auth;

import java.time.Instant;

import com.backend.hrms.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reset_password")
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordEntity extends BaseEntity {

    // here it is designed in a way that primary key itself is an token

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = true, name = "used_at")
    private Instant usedAt;

    // this is mail sent status
    @Column(nullable = false)
    private boolean sent = false;

    @Column(nullable = false, name = "sent_at")
    private Instant sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;
}
