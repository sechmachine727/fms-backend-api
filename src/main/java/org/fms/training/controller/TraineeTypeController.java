package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.TraineeType;
import org.fms.training.service.TraineeTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainee-types")
@RequiredArgsConstructor
public class TraineeTypeController {

    private final TraineeTypeService traineeTypeService;

    @GetMapping
    public ResponseEntity<List<TraineeType>> getAllTraineeTypes() {
        Optional<List<TraineeType>> traineeTypesOpt = traineeTypeService.getAllTraineeTypes();
        return traineeTypesOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
