package com.backend.hrms.dto.auth;

import java.util.List;

import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.dto.media.MediaDTO;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.media.MediaEntity;

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
    @Builder
    public static class UpdatePasswordDTO {
        @NotBlank(message = "Old Password is required")
        private String oldPassword;

        @NotBlank(message = "New Password is required")
        private String newPassword;
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
        private CompanyAdminDTO.Response companyAdmin;
        private CompanyEmployeeDTO.Response companyEmployee;
        private List<MediaEntity> media;

        public static MeDTO fromEntity(AuthEntity entity) {
            return MeDTO.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .email(entity.getEmail())
                    .phone(entity.getPhone())
                    .role(entity.getRole().name())
                    .admin(entity.getAdmin() != null ? AdminDTO.Response.fromEntity(entity.getAdmin()) : null)
                    .companyAdmin(entity.getCompanyAdmin() != null
                            ? CompanyAdminDTO.Response.fromShallowEntity(entity.getCompanyAdmin())
                            : null)
                    .companyEmployee(entity.getCompanyEmployee() != null
                            ? CompanyEmployeeDTO.Response.fromShallowEntity(entity.getCompanyEmployee())
                            : null)
                    .media(entity.getMedia())
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileUpdateDTO {
        @NotBlank(message = "First name is required")
        private String firstName;

        private String middleName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        // this is correct as what media controller returns should be here
        private List<MediaDTO.Response> media;
    }

}
