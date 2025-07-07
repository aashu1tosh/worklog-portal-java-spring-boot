package com.backend.hrms.controller.company.worklog;

import java.util.UUID;

import org.hibernate.query.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.worklog.WorklogDTO;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.service.company.worklog.WorklogService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/company/worklog")
@AllArgsConstructor
public class WorklogController {

    private final WorklogService worklogService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('COMPANY_EMPLOYEE')")
    public ApiResponse<String> create(@Valid @RequestBody WorklogDTO.RegisterDTO body,
            @AuthenticationPrincipal JwtPayload jwt) {

        worklogService.create(body, jwt);
        return new ApiResponse<>(true, Messages.CREATED, "");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
    public ApiResponse<WorklogDTO.ResponseDTO> getByEmployeeId(@AuthenticationPrincipal JwtPayload jwt,
            Pageable pageable,
            @PathVariable UUID id) {
        var worklog = worklogService.getByEmployee(id, pageable, jwt);
        return new ApiResponse<>(true, Messages.SUCCESS, WorklogDTO.ResponseDTO.fromEntity(worklog));
    }

}
