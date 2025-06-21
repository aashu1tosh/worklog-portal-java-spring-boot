package com.backend.hrms.controller.company;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.dto.paginatedResponse.PaginatedResponse;
import com.backend.hrms.entity.company.CompanyEntity;
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
    public ApiResponse<PaginatedResponse<CompanyDTO.Response>> get(
            @AuthenticationPrincipal JwtPayload jwt,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(name = "search", defaultValue = "") String search) {

        Page<CompanyEntity> data = companyService.get(pageable, search);

        List<CompanyDTO.Response> company = data.getContent().stream()
                .map(CompanyDTO.Response::fromEntity)
                .toList();

        PaginatedResponse<CompanyDTO.Response> paginatedResponse = new PaginatedResponse<>(
                company,
                new PaginatedResponse.Pagination(
                        data.getNumber() + 1,
                        data.getSize(),
                        data.getTotalElements(),
                        data.getTotalPages()));

        return new ApiResponse<PaginatedResponse<CompanyDTO.Response>>(true, Messages.SUCCESS, paginatedResponse);
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyDTO.Response> getById(
            @AuthenticationPrincipal JwtPayload jwt,
            @PathVariable UUID id) {

        CompanyEntity companyEntity = companyService.getById(id);

        CompanyDTO.Response companyResponse = CompanyDTO.Response.fromEntity(companyEntity);

        return new ApiResponse<CompanyDTO.Response>(true, Messages.SUCCESS, companyResponse);
    }

    @PatchMapping("/{id}")
    public ApiResponse<String> updateCompany(
            @AuthenticationPrincipal JwtPayload jwt,
            @PathVariable UUID id,
            @Valid @RequestBody CompanyDTO.Update body) {

        companyService.update(id, body);

        return new ApiResponse<>(true, Messages.UPDATED, "");
    }

}
