package org.fms.training.service;

import org.fms.training.common.entity.TraineeType;

import java.util.List;
import java.util.Optional;

public interface TraineeTypeService {
    Optional<List<TraineeType>> getAllTraineeTypes();
}
