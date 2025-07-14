package com.backend.hrms.repository.media;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.media.MediaEntity;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, UUID> {

    Optional<MediaEntity> findById(UUID id);

    void deleteById(UUID id);
}
