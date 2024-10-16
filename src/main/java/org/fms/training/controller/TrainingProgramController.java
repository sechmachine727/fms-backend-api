package org.fms.training.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.fms.training.config.Authorization;
import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.enums.TrainingProgramStatus;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.repository.TrainingProgramRepository;
import org.fms.training.service.TrainingProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {
    private final TrainingProgramService trainingProgramService;
    private final TrainingProgramRepository trainingProgramRepository;
    @RolesAllowed({Authorization.CONTENT_MANAGER, Authorization.FA_MANAGER, Authorization.DELIVERABLES_MANAGER, Authorization.GROUP_ADMIN})
    @GetMapping
    public ResponseEntity<List<ListTrainingProgramDTO>> getAllTrainingPrograms(
            @RequestParam(required = false) String search) {
        Optional<List<ListTrainingProgramDTO>> result = trainingProgramService.getAllTrainingPrograms(search);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER, Authorization.FA_MANAGER, Authorization.DELIVERABLES_MANAGER, Authorization.GROUP_ADMIN, Authorization.TRAINER})
    @GetMapping("/{id}")
    public ResponseEntity<ReadTrainingProgramDTO> getByTrainingProgramId(@PathVariable Integer id) {
        Optional<ReadTrainingProgramDTO> result = trainingProgramService.getTrainingProgramById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER})
    @PostMapping
    public ResponseEntity<Map<String, String>> createTrainingProgram(@RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        trainingProgramService.createTrainingProgram(saveTrainingProgramDTO);
        Map<String, String> responseSuccess = Map.of("success", "Create training program success");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseSuccess);
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER})
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateTrainingProgram(@PathVariable Integer id, @RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        trainingProgramService.updateTrainingProgram(id, saveTrainingProgramDTO);
        Map<String, String> responseSuccess = Map.of("success", "Update training program success");
        return ResponseEntity.ok(responseSuccess);
    }

    @GetMapping("/technical-group/{technicalGroupId}")
    public ResponseEntity<List<ListByTechnicalGroupDTO>> findByTechnicalGroupId(@PathVariable Integer technicalGroupId) {
        Optional<List<ListByTechnicalGroupDTO>> result = Optional.ofNullable(trainingProgramService.findByTechnicalGroupId(technicalGroupId));
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER, Authorization.FA_MANAGER})
    @PutMapping("/toggle-activate/{id}")
    public ResponseEntity<Map<String, String>> updateTrainingProgramStatus(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            TrainingProgramStatus newStatus = trainingProgramService.toggleTrainingProgramStatusToActiveAndInactive(id);
            response.put("success", "Training program status updated successfully to " + newStatus);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Update training program status failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @RolesAllowed({Authorization.FA_MANAGER})
    @PutMapping("/decline/{id}")
    public ResponseEntity<Map<String, String>> toggleReviewingToDeclined(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            TrainingProgramStatus newStatus = trainingProgramService.toggleTrainingProgramStatusFromReviewingToDeclined(id);
            response.put("success", "Training program status updated successfully to " + newStatus);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Update training program status failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER, Authorization.FA_MANAGER})
    @PutMapping("/approve/{id}")
    public ResponseEntity<Map<String, String>> toggleReviewingToActive(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            TrainingProgramStatus newStatus = trainingProgramService.toggleTrainingProgramStatusFromReviewingOrDeclinedToActive(id);
            response.put("success", "Training program status updated successfully to " + newStatus);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Update training program status failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}