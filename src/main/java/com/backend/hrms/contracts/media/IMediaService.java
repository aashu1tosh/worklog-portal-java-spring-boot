package com.backend.hrms.contracts.media;

import java.util.List;

import com.backend.hrms.dto.media.MediaDTO;
import com.backend.hrms.entity.media.MediaEntity;

public interface IMediaService {

    MediaEntity uploadFile(MediaDTO.Response data);

    <T> void uploadMultipleFiles(List<MediaDTO.Response> mediaList, T entity, String entityField);

    void delete(String idString);

    Boolean deleteMultipleFile(List<String> ids);
}
