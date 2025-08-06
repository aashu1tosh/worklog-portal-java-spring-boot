package com.backend.hrms.service.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.hrms.contracts.auth.IAuthService;
import com.backend.hrms.contracts.company.ICompanyAdminService;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.entity.company.CompanyAdminEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.company.CompanyAdminRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
class CompanyAdminService implements ICompanyAdminService {

    private final CompanyAdminRepository companyAdminRepository;
    private final IAuthService authService;
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

    public void update(AuthDTO.ProfileUpdateDTO data, UUID id) {
        var auth = authService.checkById(id);

        if (auth.getCompanyAdmin().getId() == null) {
            throw HttpException.notFound("Admin not found for the given ID.");
        }

        var adminEntity = auth.getCompanyAdmin();
        adminEntity.setFirstName(data.getFirstName());
        adminEntity.setLastName(data.getLastName());
        adminEntity.setMiddleName(data.getMiddleName());

        companyAdminRepository.save(adminEntity);
    }
}
