package com.backend.hrms.contracts.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.entity.company.CompanyEntity;

public interface ICompanyService {

    void create(CompanyDTO.Create data);

    Page<CompanyEntity> get(Pageable pageable, String search);

    CompanyEntity getById(UUID id);

    void update(UUID id, CompanyDTO.Update data);
}
