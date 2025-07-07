package com.backend.hrms.repository.company.worklog;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.worklog.WorklogEntity;

@Repository
public interface WorklogRepository extends JpaRepository<WorklogEntity, UUID> {

}
