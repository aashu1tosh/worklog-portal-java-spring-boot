package com.backend.hrms.controller.media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.hrms.constants.enums.MediaType;
import com.backend.hrms.dto.apiResponse.ApiResponse;
import com.backend.hrms.dto.media.MediaDTO;
import com.backend.hrms.exception.HttpException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final String TEMP_UPLOAD_DIR = new File(System.getProperty("user.dir"), "uploads/temp").getAbsolutePath();

    @PostMapping()
    public ApiResponse<List<MediaDTO.Response>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @Valid @ModelAttribute MediaDTO.Upload uploadRequest // Use @ModelAttribute with your DTO
    ) {
        if (files.length == 0) {
            throw HttpException.badRequest("No files provided for upload");
        }

        MediaType type = uploadRequest.getType();
        String description = uploadRequest.getDescription();

        File tempDir = new File(TEMP_UPLOAD_DIR);
        if (!tempDir.exists()) {
            // This will now create the directory at the absolute path specified
            // Ensure the user running the app has permissions to create this directory.
            boolean created = tempDir.mkdirs();
            if (!created) {

                throw HttpException.internalServerError(
                        "Internal Server Error");
            }
        }

        List<MediaDTO.Response> uploadedFileResponses = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();

            if (file.isEmpty()) {
                throw HttpException
                        .badRequest("File '" + originalFilename + "' is empty. Please provide a valid file.");
            }

            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                int lastDotIndex = originalFilename.lastIndexOf('.');
                if (lastDotIndex > 0) {
                    fileExtension = originalFilename.substring(lastDotIndex);
                }
            }

            String tempFileName = System.currentTimeMillis() + "_" + Math.round(Math.random() * 1e9) + fileExtension;
            // Construct full path using the absolute base path
            String tempFullPath = TEMP_UPLOAD_DIR + File.separator + tempFileName;

            File tempDestFile = new File(tempFullPath);
            try {
                file.transferTo(tempDestFile);

                uploadedFileResponses.add(MediaDTO.Response.builder()
                        .originalFileName(originalFilename)
                        .name(tempFileName)
                        .mimeType(file.getContentType())
                        .type(type)
                        .description(description)
                        .build());

            } catch (IOException e) {
                throw HttpException.internalServerError(
                        "Failed to upload '" + originalFilename + "' to temporary storage: ");
            } catch (IllegalStateException e) {
                throw HttpException.internalServerError(
                        "Illegal state during temp upload for '" + originalFilename + "': ");
            } catch (Exception e) {
                // More detailed message for generic exceptions
                throw HttpException.internalServerError("An unexpected error occurred during temp upload for '"
                        + originalFilename + "': " + e.getMessage());
            }
        }

        return new ApiResponse<>(true, "Files uploaded successfully to temporary storage.", uploadedFileResponses);
    }

}