package org.fms.training.service;

import org.fms.training.common.dto.trainerdto.ListTrainerDTO;
import org.fms.training.common.dto.trainerdto.ReadTrainerDTO;
import org.fms.training.common.dto.trainerdto.SaveTrainerDTO;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void addTrainer(SaveTrainerDTO saveTrainerDTO);

    void updateTrainer(Integer id, SaveTrainerDTO saveTrainerDTO);

    List<ListTrainerDTO> getAllTrainers();

    Optional<ReadTrainerDTO> getTrainerById(Integer id);
}
