package com.backend.hrms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.ApiResponse.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/public/login")
    public ApiResponse<String> login() {
        System.out.println("Login endpoint hit");
        return new ApiResponse<String>(true, "Login endpoint is not implemented yet.", "");
    }

    @PostMapping("/public/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> register() {
        System.out.println("Register endpoint hit");
        return new ApiResponse<String>(true, "Register endpoint is not implemented yet.", "");
    }
}
