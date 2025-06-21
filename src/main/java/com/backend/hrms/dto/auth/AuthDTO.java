package com.backend.hrms.dto.auth;

import com.backend.hrms.dto.AdminDTO;
import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.auth.AuthEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class AuthDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginDTO {
        @Email(message = "Must be a valid e‑mail address")
        @NotBlank(message = "E‑mail is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class MeDTO extends BaseResponse {
        private String email;
        private String role;
        private String phone;
        private AdminDTO.Response admin;

        public static MeDTO fromEntity(AuthEntity entity) {
            return MeDTO.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .email(entity.getEmail())
                    .phone(entity.getPhone())
                    .role(entity.getRole().name())
                    .admin(entity.getAdmin() != null ? AdminDTO.Response.fromEntity(entity.getAdmin()) : null)
                    .build();
        }
    }
}
