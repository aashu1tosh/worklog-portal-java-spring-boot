package com.backend.hrms.service.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.entity.company.CompanyEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.company.CompanyRepository;

@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(
            CompanyRepository companyRepository

    ) {
        this.companyRepository = companyRepository;
    }

    public void create(CompanyDTO.Create data) {

        if (this.companyRepository.findByEmail(data.getEmail()).isPresent())
            throw HttpException.badRequest("Company with this email already exists");

        if (this.companyRepository.findByPhone(data.getPhone()).isPresent())
            throw HttpException.badRequest("Company with this phone number already exists");

        CompanyEntity company = new CompanyEntity();

        company.setName(data.getName());
        company.setAddress(data.getAddress());
        company.setPhone(data.getPhone());
        company.setEmail(data.getEmail());

        this.companyRepository.save(company);
        return;
    }

    public Page<CompanyEntity> get(Pageable pageable, String search) {
        Page<CompanyEntity> companies = this.companyRepository.findByNameContainingIgnoreCase(pageable, search);
        return companies;
    }

    public CompanyEntity getById(UUID id) {
        return this.companyRepository.findById(id)
                .orElseThrow(() -> HttpException.notFound("Company not found"));
    }

    public void update(UUID id, CompanyDTO.Update data) {

        CompanyEntity company = this.getById(id);

        if (data.getName() != null && !data.getName().isBlank()) {
            company.setName(data.getName());
        }

        if (data.getAddress() != null && !data.getAddress().isBlank()) {
            company.setAddress(data.getAddress());
        }

        if (data.getEmail() != null && !data.getEmail().isBlank()) {
            if (this.companyRepository.findByEmailAndIdNot(id, data.getEmail()).isPresent())
                throw HttpException.badRequest("Company with this email already exists");
            company.setEmail(data.getEmail());
        }

        if (data.getPhone() != null && !data.getPhone().isBlank()) {
            if (this.companyRepository.findByPhoneAndIdNot(id, data.getPhone()).isPresent())
                throw HttpException.badRequest("Company with this phone number already exists");
            company.setPhone(data.getPhone());
        }

        this.companyRepository.save(company);
        return;
    }
}
