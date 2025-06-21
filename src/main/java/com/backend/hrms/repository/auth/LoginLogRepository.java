package com.backend.hrms.repository.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.auth.LoginLogEntity;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLogEntity, UUID> {

    Optional<LoginLogEntity> findById(UUID id);
}
