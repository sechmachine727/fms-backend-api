package org.fms.training.mapper;

import org.fms.training.dto.groupdto.ListGroupDTO;
import org.fms.training.dto.groupdto.ReadGroupDTO;
import org.fms.training.dto.groupdto.SaveGroupDTO;
import org.fms.training.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    @Mapping(source = "trainingProgram.trainingProgramName", target = "trainingProgramName")
    @Mapping(source = "site.siteName", target = "siteName")
    @Mapping(source = "deliveryType.deliveryTypeName", target = "deliveryTypeName")
    @Mapping(source = "traineeType.traineeTypeName", target = "traineeTypeName")
    @Mapping(source = "scope.scopeName", target = "scopeName")
    @Mapping(source = "formatType.formatTypeName", target = "formatTypeName")
    @Mapping(source = "keyProgram.keyProgramName", target = "keyProgramName")
    @Mapping(source = "userGroups", target = "employeeIds", qualifiedByName = "toEmployeeId")
    ReadGroupDTO toReadGroupDTO(Group group);

    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    @Mapping(source = "trainingProgram.trainingProgramName", target = "trainingProgramName")
    @Mapping(source = "site.siteName", target = "siteName")
    @Mapping(source = "userGroups", target = "classAdminEmployeeId")
    ListGroupDTO toListGroupDTO(Group group);

    @Mapping(target = "trainingProgram", source = "trainingProgramId")
    @Mapping(target = "site", source = "siteId")
    @Mapping(target = "deliveryType", source = "deliveryTypeId")
    @Mapping(target = "traineeType", source = "traineeTypeId")
    @Mapping(target = "scope", source = "scopeId")
    @Mapping(target = "formatType", source = "formatTypeId")
    @Mapping(target = "keyProgram", source = "keyProgramId")
    Group toGroupEntity(SaveGroupDTO saveGroupDTO);

    @Named("toEmployeeId")
    default String convertToEmployeeId(UserGroup userGroup) {
        return userGroup.getUser().getEmployeeId();
    }

    default List<String> convertToClassAdminEmployeeId(List<UserGroup> userGroups) {
        return userGroups.stream()
                .map(userGroup -> {
                    User user = userGroup.getUser();
                    for (UserRole role : user.getUserRoles()) {
                        if (role.getRole().getRoleName().equals("CLASSADMIN")) {
                            return user.getEmployeeId();
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    default TrainingProgram mapTrainingProgram(Integer id) {
        if (id == null) {
            return null;
        }
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setId(id);
        return trainingProgram;
    }

    default Site mapSite(Integer id) {
        if (id == null) {
            return null;
        }
        Site site = new Site();
        site.setId(id);
        return site;
    }

    default DeliveryType mapDeliveryType(Integer id) {
        if (id == null) {
            return null;
        }
        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setId(id);
        return deliveryType;
    }

    default TraineeType mapTraineeType(Integer id) {
        if (id == null) {
            return null;
        }
        TraineeType traineeType = new TraineeType();
        traineeType.setId(id);
        return traineeType;
    }

    default Scope mapScope(Integer id) {
        if (id == null) {
            return null;
        }
        Scope scope = new Scope();
        scope.setId(id);
        return scope;
    }

    default FormatType mapFormatType(Integer id) {
        if (id == null) {
            return null;
        }
        FormatType formatType = new FormatType();
        formatType.setId(id);
        return formatType;
    }

    default KeyProgram mapKeyProgram(Integer id) {
        if (id == null) {
            return null;
        }
        KeyProgram keyProgram = new KeyProgram();
        keyProgram.setId(id);
        return keyProgram;
    }
}
