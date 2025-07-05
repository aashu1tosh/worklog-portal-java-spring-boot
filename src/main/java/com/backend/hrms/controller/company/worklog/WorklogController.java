package com.backend.hrms.controller.company.worklog;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.helpers.Messages;
import com.backend.hrms.security.jwt.JwtPayload;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/company/worklog")
@AllArgsConstructor
public class WorklogController {

    @PostMapping()
    public ApiResponse<String> create(@Valid @RequestBody CompanyDTO.Create body,
            @AuthenticationPrincipal JwtPayload jwt) {
        
        
        return new ApiResponse<>(true, Messages.CREATED, "");
    }

}
