package com.backend.hrms.controller.company;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.service.company.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN')")
    public ApiResponse<String> createCompany(@Valid @RequestBody CompanyDTO.Create body,
            @AuthenticationPrincipal JwtPayload jwt1) {
        
        companyService.create(body);
        return new ApiResponse<>(true, Messages.CREATED, "");
    }

    @GetMapping()
    public ApiResponse<String> getCompany(
            @AuthenticationPrincipal JwtPayload jwt) {
        
        
        return new ApiResponse<>(true, Messages.SUCCESS, "");
    }
}
