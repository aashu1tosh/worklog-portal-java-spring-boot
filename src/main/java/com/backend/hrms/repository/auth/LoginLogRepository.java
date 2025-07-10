package com.backend.hrms.repository.auth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.auth.LoginLogEntity;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLogEntity, UUID> {

    Optional<LoginLogEntity> findById(UUID id);

    Page<LoginLogEntity> findAllByAuthId(UUID authId, Pageable pageable);

    @Query("SELECT l FROM LoginLogEntity l WHERE l.authId = :authId AND l.logout IS NULL")
    List<LoginLogEntity> findLoggedInSessionsByAuthId(@Param("authId") UUID authId);

    Optional<LoginLogEntity> findByIdAndAuthId(UUID id, UUID authId);
}
