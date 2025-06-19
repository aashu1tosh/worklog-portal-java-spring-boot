package com.backend.hrms.entity.company;

import com.backend.hrms.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class CompanyEntity extends BaseEntity {

    @Column(name = "company_name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_number")
    private String phone;

    @Column(name = "contact_email")
    private String email;
}
