package com.backend.hrms.contracts.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.entity.company.CompanyEmployeeEntity;

public interface ICompanyEmployeeService {

    void register(CompanyEmployeeDTO.RegisterDTO data, UUID companyId);

    Page<CompanyEmployeeEntity> get(Pageable pageable, String search, UUID companyId);

    CompanyEmployeeEntity getById(UUID id);

    void update(AuthDTO.ProfileUpdateDTO data, UUID id);
}
