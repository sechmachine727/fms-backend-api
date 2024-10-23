package org.fms.training.common.mapper;

import org.fms.training.common.dto.trainerdto.ListTrainerDTO;
import org.fms.training.common.dto.trainerdto.ReadTrainerDTO;
import org.fms.training.common.dto.trainerdto.SaveTrainerDTO;
import org.fms.training.common.entity.Trainer;
import org.fms.training.common.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    @Mapping(source = "user.account", target = "trainerAccount")
    @Mapping(source = "user.employeeId", target = "trainerEmployeeId")
    @Mapping(source = "user.name", target = "trainerName")
    @Mapping(source = "user.email", target = "trainerEmail")
    @Mapping(source = "user.status", target = "trainerStatus")
    ListTrainerDTO toListTrainerDTO(Trainer trainer);

    @Mapping(source = "user.account", target = "trainerAccount")
    @Mapping(source = "user.employeeId", target = "trainerEmployeeId")
    @Mapping(source = "user.name", target = "trainerName")
    @Mapping(source = "user.email", target = "trainerEmail")
    @Mapping(source = "user.contractType", target = "contractType")
    @Mapping(source = "user.status", target = "trainerStatus")
    ReadTrainerDTO toReadTrainerDTO(Trainer trainer);

    void updateTrainerEntityFromDTO(SaveTrainerDTO saveTrainerDTO, @MappingTarget Trainer trainer);

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    Trainer toTrainerEntity(SaveTrainerDTO saveTrainerDTO);

    @Named("mapUser")
    default User map(Integer userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
