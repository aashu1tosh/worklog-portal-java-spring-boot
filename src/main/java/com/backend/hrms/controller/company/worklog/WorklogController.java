package com.backend.hrms.controller.company.worklog;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.contracts.company.worklog.IWorklogService;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.worklog.WorklogDTO;
import com.backend.hrms.dto.paginatedResponse.PaginatedResponse;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/company/worklog")
@AllArgsConstructor
public class WorklogController {

        private final IWorklogService worklogService;

        @PostMapping()
        @PreAuthorize("hasAnyRole('COMPANY_EMPLOYEE')")
        public ApiResponse<String> create(@Valid @RequestBody WorklogDTO.RegisterDTO body,
                        @AuthenticationPrincipal JwtPayload jwt) {

                worklogService.create(body, jwt);
                return new ApiResponse<>(true, Messages.CREATED, "");
        }

        @PatchMapping("/{id}")
        @PreAuthorize("hasAnyRole('COMPANY_EMPLOYEE')")
        public ApiResponse<String> update(@Valid @RequestBody WorklogDTO.UpdateDTO body,
                        @PathVariable UUID id,
                        @AuthenticationPrincipal JwtPayload jwt) {

                worklogService.update(id, body, jwt);
                return new ApiResponse<>(true, Messages.CREATED, "");
        }

        @GetMapping()
        @PreAuthorize("hasAnyRole('COMPANY_EMPLOYEE')")
        public ApiResponse<PaginatedResponse<WorklogDTO.Response>> get(@AuthenticationPrincipal JwtPayload jwt,
                        Pageable pageable) {

                var worklogs = worklogService.get(pageable, jwt);

                var data = worklogs.getContent().stream()
                                .map(WorklogDTO.Response::fromEntity)
                                .toList();

                var paginatedResponse = new PaginatedResponse<WorklogDTO.Response>(
                                data,
                                new PaginatedResponse.Pagination(
                                                worklogs.getNumber() + 1,
                                                worklogs.getSize(),
                                                worklogs.getTotalElements(),
                                                worklogs.getTotalPages()));
                return new ApiResponse<>(true, Messages.SUCCESS, paginatedResponse);
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('COMPANY_EMPLOYEE')")
        public ApiResponse<WorklogDTO.Response> getById(@AuthenticationPrincipal JwtPayload jwt,
                        @PathVariable UUID id) {
                var worklog = worklogService.getById(id, jwt);

                return new ApiResponse<WorklogDTO.Response>(
                                true,
                                Messages.SUCCESS,
                                WorklogDTO.Response.fromEntity(worklog));

        }

        @GetMapping("/employee/{id}")
        @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'COMPANY_SUPER_ADMIN')")
        public ApiResponse<PaginatedResponse<WorklogDTO.Response>> getByEmployeeId(
                        @AuthenticationPrincipal JwtPayload jwt,
                        Pageable pageable,
                        @PathVariable UUID id) {

                var worklog = worklogService.getByEmployee(id, pageable, jwt);

                if (worklog.isEmpty() || worklog.getTotalElements() == 0) {
                        throw HttpException.notFound("No worklogs found for this employee.");
                }

                var data = worklog.getContent().stream()
                                .map(WorklogDTO.Response::fromEntity)
                                .toList();

                var paginatedResponse = new PaginatedResponse<WorklogDTO.Response>(
                                data,
                                new PaginatedResponse.Pagination(
                                                worklog.getNumber() + 1,
                                                worklog.getSize(),
                                                worklog.getTotalElements(),
                                                worklog.getTotalPages()));
                return new ApiResponse<PaginatedResponse<WorklogDTO.Response>>(true, Messages.SUCCESS,
                                paginatedResponse);
        }

}
