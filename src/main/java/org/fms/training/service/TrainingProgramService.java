package org.fms.training.service;

import org.fms.training.common.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.common.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.common.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.common.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.common.enums.TrainingProgramStatus;
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