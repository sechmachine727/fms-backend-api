package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainerdto.ListTrainerDTO;
import org.fms.training.common.dto.trainerdto.ListUserToAddDTO;
import org.fms.training.common.dto.trainerdto.ReadTrainerDTO;
import org.fms.training.common.dto.trainerdto.SaveTrainerDTO;
import org.fms.training.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping
    public ResponseEntity<List<ListTrainerDTO>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadTrainerDTO> getTrainerById(@PathVariable Integer id) {
        return trainerService.getTrainerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addTrainer(@RequestBody SaveTrainerDTO saveTrainerDTO) {
        trainerService.addTrainer(saveTrainerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Add trainer success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTrainer(@PathVariable Integer id, @RequestBody SaveTrainerDTO saveTrainerDTO) {
        trainerService.updateTrainer(id, saveTrainerDTO);
        return ResponseEntity.ok("Update trainer success");
    }

    @GetMapping("/users-to-add")
    public ResponseEntity<List<ListUserToAddDTO>> getAllUserWithTrainerRoleExcludingHavingTrainerId() {
        return ResponseEntity.ok(trainerService.getAllUserWithTrainerRoleExcludingHavingTrainerId());
    }
}
