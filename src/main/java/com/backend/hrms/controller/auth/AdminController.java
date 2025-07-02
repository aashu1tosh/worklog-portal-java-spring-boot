package com.backend.hrms.controller.auth;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.auth.AdminDTO;
import com.backend.hrms.dto.paginatedResponse.PaginatedResponse;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.service.AdminService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('SUDO_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register(@Valid @RequestBody AdminDTO.RegisterDTO body) {
        if (!Role.ADMIN.equals(body.getRole()))
            throw HttpException.forbidden("Invalid role only ADMIN role is allowed for registration.");

        adminService.register(body);
        return new ApiResponse<String>(true, Messages.CREATED, "");
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('SUDO_ADMIN', 'ADMIN')")
    public ApiResponse<PaginatedResponse<AdminDTO.Response>> get(
            Pageable pageable,
            @RequestParam(name = "search", defaultValue = "") String search) {
        var response = adminService.get(pageable, search);

        var data = response.getContent().stream()
                .map(AdminDTO.Response::fromEntity)
                .toList();

        var paginatedResponse = new PaginatedResponse<AdminDTO.Response>(
                data,
                new PaginatedResponse.Pagination(
                        response.getNumber() + 1,
                        response.getSize(),
                        response.getTotalElements(),
                        response.getTotalPages()));

        return new ApiResponse<PaginatedResponse<AdminDTO.Response>>(true,
                Messages.SUCCESS, paginatedResponse);
    }

}
