package com.backend.hrms.service.company;

import org.springframework.stereotype.Service;

import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.entity.company.CompanyAdminEntity;
import com.backend.hrms.repository.company.CompanyAdminRepository;
import com.backend.hrms.service.auth.AuthService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CompanyAdminService {

    private final CompanyAdminRepository companyAdminRepository;
    private final AuthService authService;

    public void register(CompanyAdminDTO.RegisterDTO data) {
        CompanyAdminEntity companyAdminEntity = CompanyAdminEntity.builder()
            .firstName(data.getFirstName())
            .lastName(data.getLastName())
            .middleName(data.getMiddleName())
            .build();
        companyAdminRepository.save(companyAdminEntity);

        authService.registerCompanyAdmin(data, companyAdminEntity);
    }

}
