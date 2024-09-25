package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.TraineeDTO;
import org.fms.training.entity.Trainee;
import org.fms.training.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/trainees")
@RestController()
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping
    public ResponseEntity<List<Trainee>> getAll() {
        return new ResponseEntity<>(traineeService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trainee> retrieve(@PathVariable Integer id) {
        Trainee trainee = traineeService.getTrainee(id);
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(trainee);
    }

    @PostMapping
    public ResponseEntity<String> addNewTrainee(@RequestBody TraineeDTO traineeDTO) {
        try {
            Trainee trainee = new Trainee();
            trainee.setName(traineeDTO.getName());
            trainee.setAddress(traineeDTO.getAddress());
            trainee.setEmail(traineeDTO.getEmail());
            traineeService.save(trainee);
            return ResponseEntity.status(HttpStatus.CREATED).body("Add new trainee successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add new trainee failed");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            Trainee trainee = traineeService.getTrainee(id);
            if (trainee == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trainee is not exist");
            }
            traineeService.deleteTrainee(trainee.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Delete trainee successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete trainee failed");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody TraineeDTO traineeDTO, @PathVariable Integer id) {
        try {
            Trainee existingTrainee = traineeService.getTrainee(id);
            if (existingTrainee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainee is not exist");
            }
            existingTrainee.setName(traineeDTO.getName());
            existingTrainee.setAddress(traineeDTO.getAddress());
            existingTrainee.setEmail(traineeDTO.getEmail());
            traineeService.update(existingTrainee);
            return ResponseEntity.status(HttpStatus.OK).body("Update trainee successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update trainee failed");
        }
    }


}
