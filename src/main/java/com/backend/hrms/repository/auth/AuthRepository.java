package com.backend.hrms.repository.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.auth.AuthEntity;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, UUID> {
    
    Optional<AuthEntity> findByEmail(String email);
    
    Optional<AuthEntity> findByPhone(String phone);
    
    @Query("SELECT a FROM AuthEntity a WHERE a.email = :identifier OR a.phone = :identifier")
    Optional<AuthEntity> findByEmailOrPhone(@Param("identifier") String identifier);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, UUID id);
    
}