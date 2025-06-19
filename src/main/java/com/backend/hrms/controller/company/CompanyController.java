package com.backend.hrms.controller.company;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.helpers.Messages;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> createCompany(@Valid @RequestBody CompanyDTO.Create body) {
        System.out.println("Creating company with details: " + body);
        return new ApiResponse<>(true, Messages.CREATED, "");
    }
}
