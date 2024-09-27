package org.fms.training.mapper;

import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TopicTrainingProgram;
import org.fms.training.entity.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy")
    @Mapping(source = "topicTrainingPrograms", target = "topicIds")
    ReadTrainingProgramDTO toReadTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ListTrainingProgramDTO toListTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroupId", target = "technicalGroup.id")
    TrainingProgram toTrainingProgramEntity(SaveTrainingProgramDTO saveTrainingProgramDTO);

    void updateTrainingProgramEntityFromDTO(SaveTrainingProgramDTO saveTrainingProgramDTO, @MappingTarget TrainingProgram trainingProgram);

    default Integer convertToTopicId(TopicTrainingProgram topicTrainingProgram) {
        return topicTrainingProgram.getTopic().getId();
    }

    default List<Integer> convertToTopicIdList(List<TopicTrainingProgram> topicTrainingPrograms) {
        return topicTrainingPrograms.stream()
                .map(this::convertToTopicId)
                .collect(Collectors.toList());
    }
}