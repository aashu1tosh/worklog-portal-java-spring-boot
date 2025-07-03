package com.backend.hrms.controller.company;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.exception.HttpException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/admin")
public class CompanyAdminController {

    @PostMapping()
    @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN')")
    public ApiResponse<String> register(@Valid @RequestBody CompanyAdminDTO.RegisterDTO body) {

        if (!Role.COMPANY_ADMIN.equals(body.getRole()) && !Role.COMPANY_SUPER_ADMIN.equals(body.getRole()))
            throw HttpException.forbidden(
                    "Invalid role: only COMPANY ADMIN or COMPANY SUPER ADMIN roles.");
        


        return new ApiResponse<>(true, "Company admin registered successfully", "");
    }
}
