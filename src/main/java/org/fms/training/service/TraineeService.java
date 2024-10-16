package org.fms.training.service;

import org.fms.training.common.dto.traineedto.ListTraineeDTO;
import org.fms.training.common.dto.traineedto.ReadTraineeDTO;
import org.fms.training.common.dto.traineedto.SaveTraineeDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Optional<List<ListTraineeDTO>> getAllTrainees();

    Optional<ReadTraineeDTO> getTraineeById(Integer id);

    void addTrainee(SaveTraineeDTO saveTraineeDTO);

    void updateTrainee(Integer id, SaveTraineeDTO saveTraineeDTO);
}
