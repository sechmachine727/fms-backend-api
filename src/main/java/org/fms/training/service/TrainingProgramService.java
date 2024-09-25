package org.fms.training.service;

import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainingProgramService {
    Optional<List<ListTrainingProgramDTO>> findAll();

    Optional<ReadTrainingProgramDTO> findById(Integer id);

    @Transactional
    ReadTrainingProgramDTO createTrainingProgram(SaveTrainingProgramDTO saveTrainingProgramDTO);

    @Transactional
    ReadTrainingProgramDTO updateTrainingProgram(Integer trainingProgramId, SaveTrainingProgramDTO saveTrainingProgramDTO);
}