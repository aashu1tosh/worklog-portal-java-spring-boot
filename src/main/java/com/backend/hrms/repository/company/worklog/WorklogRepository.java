package com.backend.hrms.repository.company.worklog;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.worklog.WorklogEntity;

@Repository
public interface WorklogRepository extends JpaRepository<WorklogEntity, UUID> {

    Page<WorklogEntity> findByCompanyEmployeeId(Pageable pageable, UUID employeeId);

}
