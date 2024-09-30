package org.fms.training.service;

import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainingProgramService {
    Optional<List<ListTrainingProgramDTO>> findAll(String search);

    Optional<ReadTrainingProgramDTO> findById(Integer id);

    @Transactional
    void createTrainingProgram(SaveTrainingProgramDTO saveTrainingProgramDTO);

    @Transactional
    void updateTrainingProgram(Integer trainingProgramId, SaveTrainingProgramDTO saveTrainingProgramDTO);

    List<ListByTechnicalGroupDTO> findByTechnicalGroupId(Integer technicalGroupId);
}