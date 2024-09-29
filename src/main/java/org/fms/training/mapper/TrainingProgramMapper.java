package org.fms.training.mapper;

import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.TopicInfoDTO;
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
    @Mapping(source = "topicTrainingPrograms", target = "topicInfoList")
    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MMM-YYYY")
    ReadTrainingProgramDTO toReadTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "dd-MMM-YYYY")
    ListTrainingProgramDTO toListTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroupId", target = "technicalGroup.id")
    TrainingProgram toTrainingProgramEntity(SaveTrainingProgramDTO saveTrainingProgramDTO);

    void updateTrainingProgramEntityFromDTO(SaveTrainingProgramDTO saveTrainingProgramDTO, @MappingTarget TrainingProgram trainingProgram);

    default List<TopicInfoDTO> mapTopicTrainingProgramsToTopicInfoList(List<TopicTrainingProgram> topicTrainingPrograms) {
        return topicTrainingPrograms.stream()
                .map(topicTrainingProgram -> {
                    TopicInfoDTO dto = new TopicInfoDTO();
                    dto.setId(topicTrainingProgram.getTopic().getId());
                    dto.setTopicCode(topicTrainingProgram.getTopic().getTopicCode());
                    dto.setVersion(topicTrainingProgram.getTopic().getVersion());
                    dto.setTopicName(topicTrainingProgram.getTopic().getTopicName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}