package org.fms.training.service;

import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.enums.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainingProgramService {
    Optional<List<ListTrainingProgramDTO>> getAllTrainingPrograms(String search);

    Optional<ReadTrainingProgramDTO> getTrainingProgramById(Integer id);

    @Transactional
    void createTrainingProgram(SaveTrainingProgramDTO saveTrainingProgramDTO);

    @Transactional
    void updateTrainingProgram(Integer trainingProgramId, SaveTrainingProgramDTO saveTrainingProgramDTO);

    List<ListByTechnicalGroupDTO> findByTechnicalGroupId(Integer technicalGroupId);

    @Transactional
    Status toggleTrainingProgramStatus(Integer id);

    Optional<TrainingProgram> findById(Integer id);
}