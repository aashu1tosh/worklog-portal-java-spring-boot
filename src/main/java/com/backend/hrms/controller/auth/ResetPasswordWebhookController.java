package com.backend.hrms.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.hrms.contracts.auth.IResetPasswordService;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.auth.ResetPasswordWebhookDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class ResetPasswordWebhookController {

    private final IResetPasswordService resetPasswordService;

    @PostMapping("/reset-password")
    public ApiResponse<String> handleResetPasswordWebhook(@RequestBody ResetPasswordWebhookDTO payload) {

        resetPasswordService.mailSend(payload.getId());

        return new ApiResponse<>(true, "Password update successful", "");
    }
}
