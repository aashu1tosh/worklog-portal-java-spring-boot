package com.backend.hrms.entity.company;

import java.util.ArrayList;
import java.util.List;

import com.backend.hrms.entity.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyEmployeeEntity> companyEmployees = new ArrayList<>();
}
