package com.backend.hrms.controller.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.entity.company.CompanyAdminEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.service.company.CompanyAdminService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/company/admin")
public class CompanyAdminController {

    private final CompanyAdminService companyAdminService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN')")
    public ApiResponse<String> register(@Valid @RequestBody CompanyAdminDTO.RegisterDTO body) {

        if (!Role.COMPANY_ADMIN.equals(body.getRole()) && !Role.COMPANY_SUPER_ADMIN.equals(body.getRole()))
            throw HttpException.forbidden(
                    "Invalid role: only COMPANY ADMIN or COMPANY SUPER ADMIN roles.");

        return new ApiResponse<>(true, "Company admin registered successfully", "");
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN')")
    
    public ApiResponse<String> get(Pageable pageable,
            @RequestParam(name = "search", defaultValue = "") String search) {

        Page<CompanyAdminEntity> data = companyAdminService.get(pageable, search);
        return new ApiResponse<>(true, "Company admin registered successfully", "");
    }
}
