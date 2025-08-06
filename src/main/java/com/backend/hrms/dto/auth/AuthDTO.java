package com.backend.hrms.dto.auth;

import java.io.Serializable;
import java.util.List;

import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.dto.media.MediaDTO;
import com.backend.hrms.entity.auth.AuthEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class AuthDTO {

    public static final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

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
        private List<MediaDTO.Return> media;

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
                    .media(entity.getMedia() != null
                            ? entity.getMedia().stream()
                                    .map(MediaDTO.Return::fromEntity)
                                    .toList()
                            : List.of())
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

        private List<String> deleteMedia;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ForgotPasswordDTO implements Serializable {
        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid e‑mail address")
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordEmailDTO {
        private String to;
        private String resetToken;
        private String webhookUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestorePasswordDTO {

        @NotBlank(message = "New password is required")
        @Pattern(regexp = passwordPattern, message = "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special character")
        private String newPassword;
    }
}
