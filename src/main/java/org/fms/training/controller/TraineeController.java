package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.traineedto.ListTraineeDTO;
import org.fms.training.dto.traineedto.ReadTraineeDTO;
import org.fms.training.dto.traineedto.SaveTraineeDTO;
import org.fms.training.service.TraineeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/trainees")
@RestController()
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping
    public ResponseEntity<List<ListTraineeDTO>> getAllTrainees() {
        return traineeService.getAllTrainees()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadTraineeDTO> getTraineeById(@PathVariable Integer id) {
        return traineeService.getTraineeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addTrainee(@RequestBody SaveTraineeDTO saveTraineeDTO) {
        traineeService.addTrainee(saveTraineeDTO);
        return ResponseEntity.ok("Add trainee success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTrainee(@PathVariable Integer id, @RequestBody SaveTraineeDTO saveTraineeDTO) {
        traineeService.updateTrainee(id, saveTraineeDTO);
        return ResponseEntity.ok("Update trainee success");
    }
}