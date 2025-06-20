package com.backend.hrms.service.company;

import org.hibernate.query.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
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
        System.out.println("Creating company with name: " + data.getName());

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

    public void get(Pageable pageable, String search) {
        System.out.println("Fetching company with search: " + search);
        Page<CompanyEntity> companies = this.companyRepository.findAll(pageable);
    }
}
