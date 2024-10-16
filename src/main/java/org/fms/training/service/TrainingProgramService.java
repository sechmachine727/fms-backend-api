package org.fms.training.service;

import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.enums.TrainingProgramStatus;
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
    TrainingProgramStatus toggleTrainingProgramStatusToActiveAndInactive(Integer id);

    @Transactional
    TrainingProgramStatus toggleTrainingProgramStatusFromReviewingToDeclined(Integer id, String reason);

    @Transactional
    TrainingProgramStatus toggleTrainingProgramStatusFromReviewingOrDeclinedToActive(Integer id);
}