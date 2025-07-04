package com.backend.hrms.controller.company;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.dto.paginatedResponse.PaginatedResponse;
import com.backend.hrms.entity.company.CompanyAdminEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.service.company.CompanyAdminService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/company/admin")
public class CompanyAdminController {

        private final CompanyAdminService companyAdminService;

        @PostMapping()
        @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN', 'COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
        public ApiResponse<String> register(@Valid @RequestBody CompanyAdminDTO.RegisterDTO body,
                        @AuthenticationPrincipal JwtPayload jwt) {

                Role userRole = Role.valueOf(jwt.role());
                Role requestedRole = body.getRole();

                // Rule 1: only COMPANY_ADMIN or COMPANY_SUPER_ADMIN can be registered
                if (!Role.COMPANY_ADMIN.equals(requestedRole) && !Role.COMPANY_SUPER_ADMIN.equals(requestedRole)) {
                        throw HttpException.forbidden("Invalid role: only COMPANY ADMIN or COMPANY SUPER ADMIN roles.");
                }

                // Rule 2: COMPANY_ADMIN cannot register a COMPANY_SUPER_ADMIN
                if (Role.COMPANY_ADMIN.equals(userRole) && Role.COMPANY_SUPER_ADMIN.equals(requestedRole)) {
                        throw HttpException.forbidden("COMPANY_ADMIN cannot register a COMPANY_SUPER_ADMIN.");
                }

                // Rule 3: COMPANY_ADMIN or COMPANY_SUPER_ADMIN cannot register for another
                // company
                if ((Role.COMPANY_ADMIN.equals(userRole) || Role.COMPANY_SUPER_ADMIN.equals(userRole))
                                && !Objects.equals(jwt.companyId(), body.getCompanyId().toString())) {
                        throw HttpException.forbidden("You are not allowed to register admin for a different company.");
                }

                if (!Role.COMPANY_ADMIN.equals(body.getRole()) && !Role.COMPANY_SUPER_ADMIN.equals(body.getRole()))
                        throw HttpException.forbidden(
                                        "Invalid role: only COMPANY ADMIN or COMPANY SUPER ADMIN roles.");

                companyAdminService.register(body);
                return new ApiResponse<>(true, "Company admin registered successfully", "");
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN', 'COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
        public ApiResponse<PaginatedResponse<CompanyAdminDTO.Response>> get(Pageable pageable,
                        @PathVariable UUID id,
                        @RequestParam(name = "search", defaultValue = "") String search) {

                Page<CompanyAdminEntity> response = companyAdminService.get(pageable, search, id);

                var data = response.getContent().stream()
                                .map(CompanyAdminDTO.Response::fromEntity)
                                .toList();

                PaginatedResponse<CompanyAdminDTO.Response> paginatedResponse = new PaginatedResponse<CompanyAdminDTO.Response>(
                                data,
                                new PaginatedResponse.Pagination(
                                                response.getNumber() + 1,
                                                response.getSize(),
                                                response.getTotalElements(),
                                                response.getTotalPages()));

                return new ApiResponse<PaginatedResponse<CompanyAdminDTO.Response>>(true, Messages.SUCCESS,
                                paginatedResponse);
        }
}
