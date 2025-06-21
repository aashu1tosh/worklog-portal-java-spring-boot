package com.backend.hrms.entity;

import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class AdminEntity extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(nullable = true)
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(mappedBy = "admin", fetch = FetchType.LAZY)
    private AuthEntity auth;
}
