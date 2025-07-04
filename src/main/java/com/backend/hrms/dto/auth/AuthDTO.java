package com.backend.hrms.dto.auth;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.auth.AuthEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Builder
    public static class RegisterAdminDTO {

        @Email(message = "Must be a valid e‑mail address")
        @NotBlank(message = "E‑mail is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Phone number is required")
        private String phone;

        @NotNull(message = "Role is required")
        private Role role;

        @NotBlank(message = "First name is required")
        private String firstName;

        private String middleName;

        @NotBlank(message = "Last name is required")
        private String lastName;
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

        // to avoid circular reference
        public static MeDTO fromEntityShallow(AuthEntity entity) {
            return MeDTO.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .email(entity.getEmail())
                    .phone(entity.getPhone())
                    .role(entity.getRole().name())
                    .admin(null)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BasicAuthResponse extends BaseResponse {
        private String email;
        private String role;
        private String phone;

        public static BasicAuthResponse fromEntity(AuthEntity entity) {
            return BasicAuthResponse.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .email(entity.getEmail())
                    .phone(entity.getPhone())
                    .role(entity.getRole().name())
                    .build();
        }
    }
}
