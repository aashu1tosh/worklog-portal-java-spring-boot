package com.backend.hrms.dto.auth;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.AdminEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class AdminDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Response extends BaseResponse {
        private String firstName;
        private String middleName;
        private String lastName;
        private AuthDTO.MeDTO auth;

        public static Response fromEntity(AdminEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .firstName(entity.getFirstName())
                    .middleName(entity.getMiddleName())
                    .lastName(entity.getLastName())
                    .auth(null == entity.getAuth() ? null : AuthDTO.MeDTO.fromEntity(entity.getAuth()))
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterDTO {

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
}
