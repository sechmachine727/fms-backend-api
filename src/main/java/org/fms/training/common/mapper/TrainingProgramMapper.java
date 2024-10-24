package org.fms.training.common.mapper;

import org.fms.training.common.dto.departmentdto.DepartmentDTO;
import org.fms.training.common.dto.trainingprogramdto.*;
import org.fms.training.common.entity.Department;
import org.fms.training.common.entity.TopicTrainingProgram;
import org.fms.training.common.entity.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {
    @Mapping(source = "technicalGroup", target = "technicalGroup")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "topicTrainingPrograms", target = "topicInfoList")
    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-YYYY")
    ReadTrainingProgramDTO toReadTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroup", target = "technicalGroup")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "topicTrainingPrograms", target = "topicInfoList")

    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "dd-MM-YYYY")
    ListTrainingProgramDTO toListTrainingProgramDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroupId", target = "technicalGroup.id")
    @Mapping(source = "departmentId", target = "department.id")
    TrainingProgram toTrainingProgramEntity(SaveTrainingProgramDTO saveTrainingProgramDTO);

    @Mapping(source = "trainingProgram", target = "trainingProgramNameVersion", qualifiedByName = "convertToTrainingProgramNameVersion")
    ListByTechnicalGroupDTO toListByTechnicalGroupDTO(TrainingProgram trainingProgram);

    void updateTrainingProgramEntityFromDTO(SaveTrainingProgramDTO saveTrainingProgramDTO, @MappingTarget TrainingProgram trainingProgram);

    @Named("convertToTrainingProgramNameVersion")
    default String convertToTrainingProgramNameVersion(TrainingProgram trainingProgram) {
        return trainingProgram.getTrainingProgramName() + " - " + trainingProgram.getVersion();
    }

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
                .toList();
    }


    default DepartmentDTO mapDepartment(Department department) {
        if (department == null) return null;
        return new DepartmentDTO(department.getId(), department.getDepartmentName());
    }
}