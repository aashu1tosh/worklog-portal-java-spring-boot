package com.backend.hrms.contracts.company.worklog;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.hrms.dto.company.worklog.WorklogDTO;
import com.backend.hrms.entity.company.worklog.WorklogEntity;
import com.backend.hrms.security.jwt.JwtPayload;

public interface IWorklogService {

    void create(WorklogDTO.RegisterDTO data, JwtPayload jwt);

    void update(UUID id, WorklogDTO.UpdateDTO data, JwtPayload jwt);

    WorklogEntity getById(UUID id, JwtPayload jwt);

    Page<WorklogEntity> get(Pageable pageable, JwtPayload jwt);

    Page<WorklogEntity> getByEmployee(UUID employeeId, Pageable pageable, JwtPayload jwt);
}
