package org.fms.training.mapper;

import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {

    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "createdDate", target = "createdDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy")
//    @Mapping(source = "topicTrainingPrograms", target = "topics")
    ReadTrainingProgramDTO toReadDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ListTrainingProgramDTO toListDTO(TrainingProgram trainingProgram);

    @Mapping(source = "technicalGroupId", target = "technicalGroup.id")
    @Mapping(target = "topicTrainingPrograms", ignore = true)
    TrainingProgram toEntity(SaveTrainingProgramDTO saveTrainingProgramDTO);

    void updateEntityFromDTO(SaveTrainingProgramDTO saveTrainingProgramDTO, @MappingTarget TrainingProgram trainingProgram);
}