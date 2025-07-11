package com.backend.hrms.dto.media;

import com.backend.hrms.constants.enums.MediaType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MediaDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Upload {

        @NotBlank(message = "Old Password is required")
        private String description;

        @NotNull(message = "Role is required")
        private MediaType type;
    }
}
