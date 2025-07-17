package com.backend.hrms.contracts.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.entity.company.CompanyAdminEntity;

public interface ICompanyAdminService {

    void register(CompanyAdminDTO.RegisterDTO data);

    Page<CompanyAdminEntity> get(Pageable pageable, String search, UUID companyId);

    void update(AuthDTO.ProfileUpdateDTO data, UUID id);
}
