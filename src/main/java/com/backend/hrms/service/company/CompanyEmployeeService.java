package com.backend.hrms.service.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.hrms.contracts.auth.IAuthService;
import com.backend.hrms.contracts.company.ICompanyEmployeeService;
import com.backend.hrms.contracts.company.ICompanyService;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.entity.company.CompanyEmployeeEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.company.CompanyEmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
class CompanyEmployeeService implements ICompanyEmployeeService {

    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final IAuthService authService;
    private final ICompanyService companyService;

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

    public void update(AuthDTO.ProfileUpdateDTO data, UUID id) {
        var auth = authService.checkById(id);

        if (auth.getCompanyEmployee().getId() == null) {
            throw HttpException.notFound("Employee not found for the given ID.");
        }

        var companyEmployeeEntity = auth.getCompanyEmployee();
        companyEmployeeEntity.setFirstName(data.getFirstName());
        companyEmployeeEntity.setLastName(data.getLastName());
        companyEmployeeEntity.setMiddleName(data.getMiddleName());
        companyEmployeeRepository.save(companyEmployeeEntity);
    }
}
