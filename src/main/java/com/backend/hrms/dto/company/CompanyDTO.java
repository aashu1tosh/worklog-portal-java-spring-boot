package com.backend.hrms.dto.company;

import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.company.CompanyEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class CompanyDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Create {

        @NotBlank(message = "Company Name is required")
        private String name;

        @NotBlank(message = "Company address is required")
        private String address;

        @Email(message = "Must be a valid e‑mail address")
        @NotBlank(message = "E‑mail is required")
        private String email;

        @NotBlank(message = "Contact Number is required")
        private String phone;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Response extends BaseResponse {
        private String name;
        private String address;
        private String email;
        private String phone;

        public static Response fromEntity(CompanyEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .name(entity.getName())
                    .address(entity.getAddress())
                    .email(entity.getEmail())
                    .phone(entity.getPhone())
                    .build();
        }
    }

}
