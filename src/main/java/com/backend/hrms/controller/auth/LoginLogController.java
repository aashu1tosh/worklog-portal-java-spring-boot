package com.backend.hrms.controller.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.auth.LoginLogDTO;
import com.backend.hrms.dto.paginatedResponse.PaginatedResponse;
import com.backend.hrms.entity.auth.LoginLogEntity;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;
import com.backend.hrms.service.auth.LoginLogService;

@RestController
@RequestMapping("/login-log")
public class LoginLogController {

    private final LoginLogService loginLogService;

    public LoginLogController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping()
    public ApiResponse<PaginatedResponse<LoginLogDTO.Response>> get(
            @AuthenticationPrincipal JwtPayload jwt,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<LoginLogEntity> data = loginLogService.get(pageable, UUID.fromString(jwt.id()));

        List<LoginLogDTO.Response> log = data.getContent().stream()
                .map(LoginLogDTO.Response::fromEntity)
                .toList();

        PaginatedResponse<LoginLogDTO.Response> paginatedResponse = new PaginatedResponse<LoginLogDTO.Response>(
                log,
                new PaginatedResponse.Pagination(
                        data.getNumber() + 1,
                        data.getSize(),
                        data.getTotalElements(),
                        data.getTotalPages()));

        return new ApiResponse<>(true, Messages.SUCCESS, paginatedResponse);

    }

    @PatchMapping("/logout/{id}")
    public ApiResponse<String> logout(
            @AuthenticationPrincipal JwtPayload jwt,
            @PathVariable UUID id) {

        loginLogService.logoutFromOtherDevice(id, UUID.fromString(jwt.id()));

        return new ApiResponse<>(true, Messages.SUCCESS, "");
    }
}
