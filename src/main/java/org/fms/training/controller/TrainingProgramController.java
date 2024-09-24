package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.service.TrainingProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {
    private final TrainingProgramService trainingProgramService;

    @GetMapping
    public ResponseEntity<List<ListTrainingProgramDTO>> findAll() {
        Optional<List<ListTrainingProgramDTO>> result = trainingProgramService.findAll();
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadTrainingProgramDTO> findById(@PathVariable Integer id) {
        Optional<ReadTrainingProgramDTO> result = trainingProgramService.findById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReadTrainingProgramDTO> createTrainingProgram(@RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        ReadTrainingProgramDTO result = trainingProgramService.createTrainingProgram(saveTrainingProgramDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadTrainingProgramDTO> updateTrainingProgram(@PathVariable Integer id, @RequestBody SaveTrainingProgramDTO saveTrainingProgramDTO) {
        ReadTrainingProgramDTO result = trainingProgramService.updateTrainingProgram(id, saveTrainingProgramDTO);
        return ResponseEntity.ok(result);
    }
}