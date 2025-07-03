package com.backend.hrms.entity.auth;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.entity.AdminEntity;
import com.backend.hrms.entity.base.BaseEntity;
import com.backend.hrms.entity.company.CompanyAdminEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
public class AuthEntity extends BaseEntity {

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 20)
    private String phone;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "admin_id", nullable = true)
    private AdminEntity admin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "company_admin_id", nullable = true)
    private CompanyAdminEntity companyAdmin;
}
