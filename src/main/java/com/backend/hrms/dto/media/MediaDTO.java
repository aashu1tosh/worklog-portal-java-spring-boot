package com.backend.hrms.dto.media;

import com.backend.hrms.constants.enums.MediaType;
import com.backend.hrms.dto.baseEntityResponse.BaseResponse;
import com.backend.hrms.entity.media.MediaEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder; // Don't forget Builder if you use it for Response as well
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class MediaDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Upload {
        // Files will be handled separately as MultipartFile[] in the controller
        // since they are part of multipart/form-data, not directly in this JSON body.

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Media type is required")
        private MediaType type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String originalFileName;
        private String name; // The new generated filename (permanent name)
        private String mimeType;
        private MediaType type; // From request body
        private String description; // From request body
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class Return extends BaseResponse {
        private String name;
        private String originalFileName;
        private String description;
        private String mimeType;
        private MediaType type;
        private String path;

        public static Return fromEntity(MediaEntity entity) {
            return Return.builder()
                    .id(entity.getId())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .name(entity.getName())
                    .originalFileName(entity.getOriginalFileName())
                    .description(entity.getDescription())
                    .mimeType(entity.getMimeType())
                    .type(entity.getType())
                    .path(entity.getPath())
                    .build();
        }
    }
}