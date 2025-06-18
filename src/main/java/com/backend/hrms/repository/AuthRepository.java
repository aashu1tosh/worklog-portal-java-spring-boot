package com.backend.hrms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.AuthEntity;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    
    // ========================= BASIC AUTHENTICATION QUERIES =========================
    
    /**
     * Find user by email (most common login method)
     */
    Optional<AuthEntity> findByEmail(String email);
    
    /**
     * Find user by phone number (alternative login)
     */
    Optional<AuthEntity> findByPhone(String phone);
    
    /**
     * Find user by email or phone (flexible login)
     */
    @Query("SELECT a FROM AuthEntity a WHERE a.email = :identifier OR a.phone = :identifier")
    Optional<AuthEntity> findByEmailOrPhone(@Param("identifier") String identifier);
    
    /**
     * Check if email already exists (for registration validation)
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if phone already exists (for registration validation)
     */
    boolean existsByPhone(String phone);
    
    /**
     * Check if email exists excluding current user (for profile updates)
     */
    boolean existsByEmailAndIdNot(String email, UUID id);
    
}