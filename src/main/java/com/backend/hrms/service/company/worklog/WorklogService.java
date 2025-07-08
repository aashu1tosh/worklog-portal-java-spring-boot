package com.backend.hrms.service.company.worklog;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.company.worklog.WorklogDTO;
import com.backend.hrms.entity.company.worklog.WorklogEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.utils.UUIDUtils;
import com.backend.hrms.repository.company.worklog.WorklogRepository;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.service.company.CompanyEmployeeService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class WorklogService {

    private final WorklogRepository worklogRepository;
    private final CompanyEmployeeService companyEmployeeCompanyService;

    public void create(WorklogDTO.RegisterDTO data, JwtPayload jwt) {
        var worklogEntity = new WorklogEntity();
        worklogEntity.setTaskCompleted(data.getTaskCompleted());
        worklogEntity.setTaskPlanned(data.getTaskPlanned());
        worklogEntity.setChallengingTask(data.getChallengingTask());
        worklogEntity.setCompanyEmployee(companyEmployeeCompanyService.getById(UUIDUtils.validateId(jwt.employeeId())));

        worklogRepository.save(worklogEntity);
    }

    public WorklogEntity getById(String id) {
        return worklogRepository.findById(UUIDUtils.validateId(id))
                .orElseThrow(() -> HttpException.badRequest("Worklog not found with id: " + id));
    }

    public Page<WorklogEntity> get(Pageable pageable, JwtPayload jwt) {
        if (jwt.employeeId() == null)
            throw HttpException.unauthorized("Unauthorized access. Employee ID is missing.");
        return worklogRepository.findByCompanyEmployeeId(pageable, UUIDUtils.validateId(jwt.employeeId()));
    }

    public Page<WorklogEntity> getByEmployee(UUID id, Pageable pageable, JwtPayload jwt) {
        return worklogRepository.getByEmployeeAndCompany(pageable, id, UUIDUtils.validateId(jwt.companyId()));
    }
}
