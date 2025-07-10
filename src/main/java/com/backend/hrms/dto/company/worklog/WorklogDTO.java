package com.backend.hrms.dto.company.worklog;

import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.entity.company.worklog.WorklogEntity;

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
        private String taskCompleted;
        private String taskPlanned;
        private String challengingTask;
        private boolean isToday;
        private CompanyEmployeeDTO.Response companyEmployee;

        public static Response fromEntity(WorklogEntity entity) {
            return Response.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .taskCompleted(entity.getTaskCompleted())
                    .challengingTask(entity.getChallengingTask() != null ? entity.getChallengingTask() : null)
                    .taskPlanned(entity.getTaskPlanned())
                    .isToday(entity.isToday())
                    // .companyEmployee(entity.getCompanyEmployee() != null
                    // ? CompanyEmployeeDTO.Response.fromEntity(entity.getCompanyEmployee())
                    // : null)
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

        @NotBlank(message = "Task Planned is required")
        private String taskPlanned;

        private String challengingTask;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class UpdateDTO {

        private String taskCompleted;

        private String taskPlanned;

        private String challengingTask;
    }
}
