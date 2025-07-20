package com.backend.hrms.repository.auth;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.auth.ResetPasswordEntity;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPasswordEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ResetPasswordEntity r WHERE r.auth.id = :authId AND r.expiresAt > CURRENT_TIMESTAMP")
    Boolean existsByAuthIdAndExpiryDate(UUID authId);

}
