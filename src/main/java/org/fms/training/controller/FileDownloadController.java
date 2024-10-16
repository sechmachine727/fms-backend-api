package org.fms.training.controller;

import jakarta.annotation.security.RolesAllowed;
import org.fms.training.config.Authorization;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RolesAllowed({Authorization.CONTENT_MANAGER})
@RequestMapping("/api/files")
public class FileDownloadController {

    @GetMapping("/download-template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        Path filePath = Paths.get("templates/Template_Import_Syllabus.xlsx");
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(Files.readAllBytes(filePath));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Template_Import_Syllabus.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileInputStream));
    }
}
