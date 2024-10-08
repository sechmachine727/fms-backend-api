package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.service.TrainingProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {
    private final TrainingProgramService trainingProgramService;

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
    public ResponseEntity<Map<String, String>> createTrainingProgram(@RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        trainingProgramService.createTrainingProgram(saveTrainingProgramDTO);
        Map<String, String> responseSuccess = Map.of("success", "Create training program success");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseSuccess);
    }

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
}