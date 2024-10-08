package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.enums.Status;
import org.fms.training.repository.TrainingProgramRepository;
import org.fms.training.service.TrainingProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {
    private final TrainingProgramService trainingProgramService;
    private final TrainingProgramRepository trainingProgramRepository;

    @GetMapping
    public ResponseEntity<List<ListTrainingProgramDTO>> getAllTrainingPrograms(
            @RequestParam(required = false) String search) {
        Optional<List<ListTrainingProgramDTO>> result = trainingProgramService.getAllTrainingPrograms(search);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadTrainingProgramDTO> getByTrainingProgramId(@PathVariable Integer id) {
        Optional<ReadTrainingProgramDTO> result = trainingProgramService.getTrainingProgramById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createTrainingProgram(@RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        trainingProgramService.createTrainingProgram(saveTrainingProgramDTO);
        return ResponseEntity.ok("Create training program success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTrainingProgram(@PathVariable Integer id, @RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        try {
            trainingProgramService.updateTrainingProgram(id, saveTrainingProgramDTO);
            return ResponseEntity.ok("Update training program success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/technical-group/{technicalGroupId}")
    public ResponseEntity<List<ListByTechnicalGroupDTO>> findByTechnicalGroupId(@PathVariable Integer technicalGroupId) {
        Optional<List<ListByTechnicalGroupDTO>> result = Optional.ofNullable(trainingProgramService.findByTechnicalGroupId(technicalGroupId));
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<String> updateTrainingProgramStatus(@PathVariable Integer id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body("Training Program id is required");
            }

            Optional<TrainingProgram> trainingProgramOptional = trainingProgramService.findById(id);
            if (trainingProgramOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Training Program not found");
            }

            TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Training Program not found"));

            // Toggle status between ACTIVE and INACTIVE
            if (trainingProgram.getStatus().equals(Status.ACTIVE)) {
                trainingProgramService.updateTrainingProgramStatus(id, Status.INACTIVE);
                return ResponseEntity.ok("Update training program status to: " + Status.INACTIVE);
            } else {
                trainingProgramService.updateTrainingProgramStatus(id, Status.ACTIVE);
                return ResponseEntity.ok("Update training program status to: " + Status.ACTIVE);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update training program status failed");
        }
    }

}