package com.backend.hrms.entity;

import com.backend.hrms.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AdminEntity extends BaseEntity {

    @Column(name = "full_name")
    private String fullName;

    @Column(nullable = true)
    private String middleName;

    @Column()
    private String lastName;

    @OneToOne(mappedBy = "admin", fetch = FetchType.LAZY)
    private AuthEntity auth;

}
