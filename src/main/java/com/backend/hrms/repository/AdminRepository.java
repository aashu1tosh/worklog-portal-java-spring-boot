package com.backend.hrms.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, UUID> {
    Page<AdminEntity> findByFirstNameContainingIgnoreCase(String search, Pageable pageable);

}
