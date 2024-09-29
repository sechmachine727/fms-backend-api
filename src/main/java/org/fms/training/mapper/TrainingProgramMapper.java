package org.fms.training.mapper;

import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TopicTrainingProgram;
import org.fms.training.entity.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "topicTrainingPrograms", target = "topicIds")
    ReadTrainingProgramDTO toReadTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    ListTrainingProgramDTO toListTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroupId", target = "technicalGroup.id")
    TrainingProgram toTrainingProgramEntity(SaveTrainingProgramDTO saveTrainingProgramDTO);

    void updateTrainingProgramEntityFromDTO(SaveTrainingProgramDTO saveTrainingProgramDTO, @MappingTarget TrainingProgram trainingProgram);

    default Integer convertToTopicId(TopicTrainingProgram topicTrainingProgram) {
        return topicTrainingProgram.getTopic().getId();
    }

}