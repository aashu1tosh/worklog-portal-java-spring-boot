package com.backend.hrms.service.media;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.hrms.contracts.media.IMediaService;
import com.backend.hrms.dto.media.MediaDTO;
import com.backend.hrms.entity.media.MediaEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.helpers.utils.MediaUtils;
import com.backend.hrms.helpers.utils.PathUtils;
import com.backend.hrms.helpers.utils.UUIDUtils;
import com.backend.hrms.repository.media.MediaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class MediaService implements IMediaService {

    private final MediaRepository mediaRepository;

    /**
     * Uploads a media file to the database and moves it from the temporary folder
     * to the upload folder.
     *
     * @param name     the name of the media file
     * @param mimeType the mime type of the file
     * @param type     the media type
     * @return the saved media entity
     */
    @Transactional
    public MediaEntity uploadFile(MediaDTO.Response data) {
        Path tempFilePath = Paths.get(PathUtils.getTempFolderPath(), data.getName());

        if (!Files.exists(tempFilePath)) {
            throw HttpException.badRequest("Sorry!, Media not found");
        }

        MediaEntity media = new MediaEntity();
        media.setName(data.getName());
        media.setOriginalFileName(data.getOriginalFileName());
        media.setDescription(data.getDescription());
        media.setMimeType(data.getMimeType());
        media.setType(data.getType());

        mediaRepository.save(media);

        media.transferImageFromTempToUploadFolder(media.getId().toString(), data.getType());

        return media;
    }

    /**
     * Uploads multiple media files and associates them with an entity dynamically.
     *
     * @param mediaList   list of media DTOs
     * @param entity      entity to associate the media with
     * @param entityField the field on the Media entity (e.g. "companyEmployee",
     *                    "project", etc.)
     */
    @Transactional
    public <T> void uploadMultipleFiles(List<MediaDTO.Response> mediaList, T entity, String entityField) {
        if (mediaList == null || mediaList.isEmpty())
            return;

        for (MediaDTO.Response mediaData : mediaList) {
            MediaEntity media = uploadFile(mediaData);

            try {
                // Capitalize the first letter of the entity field to match setter method name
                String setterName = "set" + capitalize(entityField);

                // Get declared field on MediaEntity to find the actual type (e.g.,
                // AuthEntity.class)
                Class<?> fieldType = MediaEntity.class.getDeclaredField(entityField).getType();

                // Use the resolved type to get the setter method
                Method setter = MediaEntity.class.getMethod(setterName, fieldType);
                setter.invoke(media, entity);
            } catch (Exception e) {
                throw HttpException.badRequest("Failed to associate media with entity: " + e.getMessage());
            }

            mediaRepository.save(media);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty())
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void delete(String idString) {
        var id = UUIDUtils.validateId(idString);
        MediaEntity media = mediaRepository.findById(id)
                .orElseThrow(() -> HttpException.notFound("Media not found with id: " + id));

        // Equivalent of transferImageFromUploadTOTempFolder
        MediaUtils.transferMediaFromUploadToTrashFolder(media.getId(), media.getName(), media.getType());

        mediaRepository.deleteById(id);

    }

    public Boolean deleteMultipleFile(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }

        for (String id : ids) {
            delete(id);
        }

        return true;
    }

}
