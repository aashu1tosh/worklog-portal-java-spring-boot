package com.backend.hrms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.ApiResponse.ApiResponse;

@RestController
public class PingController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return new ApiResponse<>(true, "Ping successful", "");
    }
}
