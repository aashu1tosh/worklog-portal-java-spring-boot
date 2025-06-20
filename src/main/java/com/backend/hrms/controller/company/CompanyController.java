package com.backend.hrms.controller.company;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SUDO_ADMIN')") 
    public ApiResponse<String> createCompany(@Valid @RequestBody CompanyDTO.Create body,
            @AuthenticationPrincipal JwtPayload jwt1) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userId = (String) auth.getPrincipal(); // ← ID
        String role = auth.getAuthorities()
                .stream().findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_UNKNOWN");

        System.out.println("User ID : " + userId);
        System.out.println("Role    : " + role);

        return new ApiResponse<>(true, Messages.CREATED, "");
    }
}
