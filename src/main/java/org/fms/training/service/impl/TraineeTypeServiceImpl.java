package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.TraineeType;
import org.fms.training.repository.TraineeTypeRepository;
import org.fms.training.service.TraineeTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TraineeTypeServiceImpl implements TraineeTypeService {

    private final TraineeTypeRepository traineeTypeRepository;

    @Override
    public Optional<List<TraineeType>> getAllTraineeTypes() {
        List<TraineeType> traineeTypes = traineeTypeRepository.findAll();
        return traineeTypes.isEmpty() ? Optional.empty() : Optional.of(traineeTypes);
    }
}
