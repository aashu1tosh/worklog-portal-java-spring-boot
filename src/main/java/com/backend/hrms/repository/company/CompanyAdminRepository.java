package com.backend.hrms.repository.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.CompanyAdminEntity;

@Repository
public interface CompanyAdminRepository extends JpaRepository<CompanyAdminEntity, UUID> {

    Page<CompanyAdminEntity> findByFirstNameContainingIgnoreCase(String search, Pageable pageable);

}
