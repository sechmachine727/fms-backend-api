package org.fms.training.mapper;

import org.fms.training.dto.traineedto.ListTraineeDTO;
import org.fms.training.dto.traineedto.ReadTraineeDTO;
import org.fms.training.dto.traineedto.SaveTraineeDTO;
import org.fms.training.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "dob", target = "dob")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "gpa", target = "gpa")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "nationalId", target = "nationalId")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "university", target = "university")
    @Mapping(source = "universityGraduationDate", target = "universityGraduationDate")
    ReadTraineeDTO toReadTraineeDTO(Trainee trainee);

    Trainee toTraineeEntity(SaveTraineeDTO saveTraineeDTO);

    ListTraineeDTO toListTraineeDTO(Trainee trainee);

    void updateTraineeFromDTO(SaveTraineeDTO saveTraineeDTO, @MappingTarget Trainee trainee);
}
