package com.backend.hrms.controller.company;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.security.jwt.JwtPayload;

import jakarta.validation.Valid;

public class CompanyEmployeeController {

    @PostMapping()
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
    public ApiResponse<String> register(@Valid @RequestBody CompanyEmployeeDTO.RegisterDTO body,
            @AuthenticationPrincipal JwtPayload jwt) {

        String companyIdStr = jwt.companyId();

        if (companyIdStr == null || companyIdStr.isBlank())
            throw HttpException.badRequest("Company is required");

        UUID companyId;
        try {
            companyId = UUID.fromString(companyIdStr);
        } catch (IllegalArgumentException ex) {
            throw HttpException.badRequest("Invalid company ID format");
        }

        return new ApiResponse<>(true, "Company admin registered successfully", "");
    }
}
