package com.backend.hrms.dto.company.worklog;

import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.dto.company.CompanyDTO;
import com.backend.hrms.entity.company.CompanyAdminEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class WorklogDTO {

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

        @NotBlank(message = "Task completed is required")
        private String taskCompleted;

        @NotBlank(message = "Password is required")
        private String taskPlanned;

        private String challengingTask;
    }
}
