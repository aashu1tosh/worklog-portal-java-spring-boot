package com.backend.hrms.controller.company;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.dto.paginatedResponse.PaginatedResponse;
import com.backend.hrms.entity.company.CompanyEmployeeEntity;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.helpers.utils.UUIDUtils;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.service.company.CompanyEmployeeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/company/employee")
public class CompanyEmployeeController {

    private final CompanyEmployeeService companyEmployeeService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
    public ApiResponse<String> register(@Valid @RequestBody CompanyEmployeeDTO.RegisterDTO body,
            @AuthenticationPrincipal JwtPayload jwt) {

        UUID companyId = UUIDUtils.validateId(jwt.companyId());

        companyEmployeeService.register(body, companyId);
        return new ApiResponse<>(true, "Company admin registered successfully", "");
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
    public ApiResponse<PaginatedResponse<CompanyEmployeeDTO.Response>> get(Pageable pageable,
            @AuthenticationPrincipal JwtPayload jwt,
            @RequestParam(name = "search", defaultValue = "") String search) {

        var companyId = UUIDUtils.validateId(jwt.companyId());
        Page<CompanyEmployeeEntity> response = companyEmployeeService.get(pageable, search, companyId);

        var data = response.getContent().stream()
                .map(CompanyEmployeeDTO.Response::fromEntity)
                .toList();

        var paginatedResponse = new PaginatedResponse<CompanyEmployeeDTO.Response>(
                data,
                new PaginatedResponse.Pagination(
                        response.getNumber() + 1,
                        response.getSize(),
                        response.getTotalElements(),
                        response.getTotalPages()));

        return new ApiResponse<PaginatedResponse<CompanyEmployeeDTO.Response>>(true, Messages.SUCCESS,
                paginatedResponse);
    }
}
