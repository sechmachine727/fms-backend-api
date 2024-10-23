package org.fms.training.common.mapper.groupmapper;

import org.fms.training.common.dto.groupdto.external.GroupTrainingProgramDTO;
import org.fms.training.common.entity.TrainingProgram;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupTrainingProgramMapper {
    GroupTrainingProgramDTO toGroupTrainingProgramDTO(TrainingProgram trainingProgram);
}
