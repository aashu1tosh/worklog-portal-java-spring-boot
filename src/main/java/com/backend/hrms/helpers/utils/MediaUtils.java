package com.backend.hrms.helpers.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.backend.hrms.constants.enums.MediaType;

public class MediaUtils {

    public static void transferMediaFromUploadToTrashFolder(UUID uuidId, String name, MediaType type) {
        var id = uuidId.toString();
        Path uploadPath = Paths.get(PathUtils.getUploadFolderPath(), type.name().toLowerCase(), id, name);
        Path trashPath = Paths.get(PathUtils.getTrashFolderPath(), type.name().toLowerCase(), id);

        try {
            if (!Files.exists(trashPath)) {
                Files.createDirectories(trashPath);
            }

            Path targetPath = trashPath.resolve(name);
            Files.move(uploadPath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Failed to move media file to trash", e);
        }
    }
}
