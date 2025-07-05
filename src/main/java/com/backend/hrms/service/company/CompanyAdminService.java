package com.backend.hrms.service.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CompanyService companyService;

    public void register(CompanyAdminDTO.RegisterDTO data) {
        CompanyAdminEntity companyAdminEntity = CompanyAdminEntity.builder()
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .middleName(data.getMiddleName())
                .company(companyService.getById(data.getCompanyId()))
                .build();
        companyAdminRepository.save(companyAdminEntity);

        authService.registerCompanyAdmin(data, companyAdminEntity);
    }

    public Page<CompanyAdminEntity> get(Pageable pageable, String search, UUID id) {
        if (search == null || search.trim().isEmpty()) {
            return companyAdminRepository.findAllByCompanyId(pageable, id);
        } else {
            return companyAdminRepository.findByFirstNameContainingIgnoreCaseAndCompanyId(search, pageable, id);
        }
    }

}
