package org.fms.training.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.fms.training.common.constant.Authorization;
import org.fms.training.service.TopicImportService;
import org.fms.training.service.TraineeImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@RestController
@RequiredArgsConstructor
@RolesAllowed({Authorization.CONTENT_MANAGER})
@RequestMapping("/api/import")
public class FileUploadController {

    private final TopicImportService topicImportService;
    private final TraineeImportService traineeImportService;

    @PostMapping(value = "/topics", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("confirm") boolean confirmUpdate) {
        try (InputStream inputStream = file.getInputStream()) {
            topicImportService.importDataFromStream(inputStream, confirmUpdate);
            return ResponseEntity.ok("File imported successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import file: " + e.getMessage());
        }
    }

    @PostMapping(value = "/trainees", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importTrainees(
            @RequestParam("file") MultipartFile file,
            @RequestParam("groupId") Integer groupId) {
        try (InputStream inputStream = file.getInputStream()) {
            traineeImportService.importTraineesFromExcel(inputStream, groupId);
            return ResponseEntity.ok("Trainees imported successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import trainees: " + e.getMessage());
        }
    }

}
