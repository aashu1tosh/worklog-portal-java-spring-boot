package com.backend.hrms.repository.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.CompanyEmployeeEntity;

@Repository
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployeeEntity, UUID> {

    Page<CompanyEmployeeEntity> findAllByCompanyId(Pageable pageable, UUID id);

    Page<CompanyEmployeeEntity> findByFirstNameContainingIgnoreCaseAndCompanyId(String search, Pageable pageable,
            UUID id);

}
