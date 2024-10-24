package org.fms.training.common.mapper;

import org.fms.training.common.dto.traineedto.ListTraineeDTO;
import org.fms.training.common.dto.traineedto.ReadTraineeDTO;
import org.fms.training.common.dto.traineedto.SaveTraineeDTO;
import org.fms.training.common.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(source = "dob", target = "dob", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "universityGraduationDate", target = "universityGraduationDate", dateFormat = "dd-MM-YYYY")
    ReadTraineeDTO toReadTraineeDTO(Trainee trainee);

    @Mapping(source = "dob", target = "dob", dateFormat = "dd-MM-yyyy")
    @Mapping(source = "universityGraduationDate", target = "universityGraduationDate", dateFormat = "dd-MM-yyyy")
    Trainee toTraineeEntity(SaveTraineeDTO saveTraineeDTO);

    @Mapping(source = "dob", target = "dob", dateFormat = "dd-MM-YYYY")
    ListTraineeDTO toListTraineeDTO(Trainee trainee);

    void updateTraineeFromDTO(SaveTraineeDTO saveTraineeDTO, @MappingTarget Trainee trainee);
}
