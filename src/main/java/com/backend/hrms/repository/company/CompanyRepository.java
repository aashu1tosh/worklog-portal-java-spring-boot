package com.backend.hrms.repository.company;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {

        Optional<CompanyEntity> findByName(String name);

        Optional<CompanyEntity> findByEmail(String email);

        Optional<CompanyEntity> findByPhone(String phone);

        Page<CompanyEntity> findByNameContainingIgnoreCase(Pageable pageable, @Param("search") String search);

        Optional<CompanyEntity> findByEmailAndIdNot(UUID id, String email);
        
        Optional<CompanyEntity> findByPhoneAndIdNot(UUID id, String phone);


}
