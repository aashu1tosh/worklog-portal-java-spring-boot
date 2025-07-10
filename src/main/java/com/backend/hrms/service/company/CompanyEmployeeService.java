package com.backend.hrms.service.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.entity.company.CompanyEmployeeEntity;
import com.backend.hrms.repository.company.CompanyEmployeeRepository;
import com.backend.hrms.service.auth.AuthService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CompanyEmployeeService {

    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final AuthService authService;
    private final CompanyService companyService;

    public void register(CompanyEmployeeDTO.RegisterDTO data, UUID companyId) {

        CompanyEmployeeEntity companyEmployeeEntity = CompanyEmployeeEntity.builder()
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .middleName(data.getMiddleName())
                .company(companyService.getById(companyId))
                .build();
        companyEmployeeRepository.save(companyEmployeeEntity);

        authService.registerCompanyEmployee(data, companyEmployeeEntity);
    }

    public Page<CompanyEmployeeEntity> get(Pageable pageable, String search, UUID id) {
        if (search == null || search.trim().isEmpty()) {
            return companyEmployeeRepository.findAllByCompanyId(pageable, id);
        } else {
            return companyEmployeeRepository.findByFirstNameContainingIgnoreCaseAndCompanyId(search, pageable, id);
        }
    }

    public CompanyEmployeeEntity getById(UUID id) {
        return companyEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company employee not found with id: " + id));
    }
}
