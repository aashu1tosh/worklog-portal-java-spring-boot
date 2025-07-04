package com.backend.hrms.dto.company;

import java.util.UUID;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.company.CompanyAdminEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class CompanyAdminDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Response extends BaseResponse {
        private String firstName;
        private String middleName;
        private String lastName;
        private AuthDTO.BasicAuthResponse auth;
        private CompanyDTO.Response company;

        public static Response fromEntity(CompanyAdminEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .firstName(entity.getFirstName())
                    .middleName(entity.getMiddleName() != null ? entity.getMiddleName() : null)
                    .lastName(entity.getLastName())
                    .auth(entity.getAuth() != null ? AuthDTO.BasicAuthResponse.fromEntity(entity.getAuth())
                            : null)
                    .company(entity.getCompany() != null ? CompanyDTO.Response.fromEntity(entity.getCompany()) : null)
                    .build();
        }

        public static Response fromShallowEntity(CompanyAdminEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .firstName(entity.getFirstName())
                    .middleName(entity.getMiddleName() != null ? entity.getMiddleName() : null)
                    .lastName(entity.getLastName())
                    .auth(null)
                    .company(entity.getCompany() != null ? CompanyDTO.Response.fromEntity(entity.getCompany()) : null)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
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

        @NotBlank(message = "First Name is required")
        private String firstName;

        private String middleName;

        @NotBlank(message = "Last Name is required")
        private String lastName;

        @NotNull(message = "Company is required")
        private UUID companyId;
    }
}
