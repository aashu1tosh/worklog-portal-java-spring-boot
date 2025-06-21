package com.backend.hrms.dto;

import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.AdminEntity;

import lombok.AllArgsConstructor;
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

        public static Response fromEntity(AdminEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .firstName(entity.getFirstName())
                    .middleName(entity.getMiddleName())
                    .lastName(entity.getLastName())
                    .build();
        }
    }
}
