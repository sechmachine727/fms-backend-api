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

    @Mapping(source = "dob", target = "dob", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "universityGraduationDate", target = "universityGraduationDate", dateFormat = "dd-MMM-YYYY")
    ReadTraineeDTO toReadTraineeDTO(Trainee trainee);

    @Mapping(source = "dob", target = "dob", dateFormat = "dd-MMM-yyyy")
    @Mapping(source = "universityGraduationDate", target = "universityGraduationDate", dateFormat = "dd-MMM-yyyy")
    Trainee toTraineeEntity(SaveTraineeDTO saveTraineeDTO);

    @Mapping(source = "dob", target = "dob", dateFormat = "dd-MMM-YYYY")
    ListTraineeDTO toListTraineeDTO(Trainee trainee);

    void updateTraineeFromDTO(SaveTraineeDTO saveTraineeDTO, @MappingTarget Trainee trainee);
}
