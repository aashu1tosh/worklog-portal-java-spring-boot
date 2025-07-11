package com.backend.hrms.controller.media;

import java.io.File;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    @PostMapping()
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        System.out.println("File upload route hit");

        if (files.length == 0) {
            return ResponseEntity.badRequest().body("No files selected");
        }

        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        StringBuilder resultMessage = new StringBuilder();

        for (MultipartFile file : files) {
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());

            if (file.isEmpty()) {
                resultMessage.append("Skipped empty file: ").append(file.getOriginalFilename()).append("\n");
                continue;
            }

            try {
                String absolutePath = new File(uploadDir + file.getOriginalFilename()).getAbsolutePath();
                file.transferTo(new File(absolutePath));
                resultMessage.append("Uploaded: ").append(file.getOriginalFilename()).append("\n");
            } catch (IOException e) {
                resultMessage.append("Failed to upload ").append(file.getOriginalFilename()).append(": ")
                        .append(e.getMessage()).append("\n");
            }
        }

        return ResponseEntity.ok(resultMessage.toString());
    }
}
