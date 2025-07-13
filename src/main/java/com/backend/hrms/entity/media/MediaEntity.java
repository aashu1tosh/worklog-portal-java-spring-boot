package com.backend.hrms.entity.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.backend.hrms.constants.enums.MediaType;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.base.BaseEntity;
import com.backend.hrms.helpers.utils.PathUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "media")
@Getter
@Setter
public class MediaEntity extends BaseEntity {

    @Column()
    private String name;

    @Column()
    private String originalFileName;

    @Column()
    private String description;

    @Column(name = "mime_type")
    private String mimeType;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media", nullable = true)
    private AuthEntity auth;

    @Transient
    private String path;

    @PostLoad
    public void loadImagePath() {
        if (type != null && getId() != null && name != null) {
            this.path = "/uploads" + "/" + type.name().toLowerCase() + "/" + getId() + "/" + name;
        }
    }

    public void transferImageFromTempToUploadFolder(String id, MediaType type) {
        Path tempPath = Paths.get(PathUtils.getTempFolderPath(), this.name);
        Path uploadPath = Paths.get(PathUtils.getUploadFolderPath(), type.name().toLowerCase(), id);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path targetPath = uploadPath.resolve(this.name);
            Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to move media file", e);
        }
    }
}
