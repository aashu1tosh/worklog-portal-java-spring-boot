package com.backend.hrms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.dto.apiResponse.ApiResponse;

@RestController
public class PingController {

    @GetMapping("/public/ping")
    public ApiResponse<String> ping() {
        return new ApiResponse<>(true, "Ping successful", "");
    }

    @GetMapping("/public/health")
    public ApiResponse<String> health() {
        throw new NullPointerException("Simulated server error for testing purposes");
        // return new ApiResponse<>(true, "Health check successful", "");  
    }
}
