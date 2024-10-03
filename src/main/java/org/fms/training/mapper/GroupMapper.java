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

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    @Mapping(source = "trainingProgram.trainingProgramName", target = "trainingProgramName")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "site.id", target = "siteId")
    @Mapping(source = "site.siteName", target = "siteName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.locationName", target = "locationName")
    @Mapping(source = "deliveryType.id", target = "deliveryTypeId")
    @Mapping(source = "deliveryType.deliveryTypeName", target = "deliveryTypeName")
    @Mapping(source = "traineeType.id", target = "traineeTypeId")
    @Mapping(source = "traineeType.traineeTypeName", target = "traineeTypeName")
    @Mapping(source = "scope.id", target = "scopeId")
    @Mapping(source = "scope.scopeName", target = "scopeName")
    @Mapping(source = "formatType.id", target = "formatTypeId")
    @Mapping(source = "formatType.formatTypeName", target = "formatTypeName")
    @Mapping(source = "keyProgram.id", target = "keyProgramId")
    @Mapping(source = "keyProgram.keyProgramName", target = "keyProgramName")
    @Mapping(source = "userGroups", target = "assignedUserIds", qualifiedByName = "toUserId")
    @Mapping(source = "userGroups", target = "assignedUserAccounts", qualifiedByName = "toAccount")
    @Mapping(source = "expectedStartDate", target = "expectedStartDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "expectedEndDate", target = "expectedEndDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "actualStartDate", target = "actualStartDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "actualEndDate", target = "actualEndDate", dateFormat = "dd-MMM-YYYY")
    ReadGroupDTO toReadGroupDTO(Group group);

    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    @Mapping(source = "trainingProgram.trainingProgramName", target = "trainingProgramName")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "traineeType.traineeTypeName", target = "traineeTypeName")
    @Mapping(source = "site.siteName", target = "siteName")
    @Mapping(source = "location.locationName", target = "locationName")
    @Mapping(source = "userGroups", target = "classAdminAccount")
    @Mapping(source = "expectedStartDate", target = "expectedStartDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "expectedEndDate", target = "expectedEndDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "actualStartDate", target = "actualStartDate", dateFormat = "dd-MMM-YYYY")
    @Mapping(source = "actualEndDate", target = "actualEndDate", dateFormat = "dd-MMM-YYYY")
    ListGroupDTO toListGroupDTO(Group group);

    @Mapping(target = "trainingProgram", source = "trainingProgramId")
    @Mapping(target = "technicalGroup", source = "technicalGroupId")
    @Mapping(target = "site", source = "siteId")
    @Mapping(target = "location", source = "locationId")
    @Mapping(target = "deliveryType", source = "deliveryTypeId")
    @Mapping(target = "traineeType", source = "traineeTypeId")
    @Mapping(target = "scope", source = "scopeId")
    @Mapping(target = "formatType", source = "formatTypeId")
    @Mapping(target = "keyProgram", source = "keyProgramId")
    Group toGroupEntity(SaveGroupDTO saveGroupDTO);

    @Named("toUserId")
    default List<Integer> convertToUserId(List<UserGroup> userGroups) {
        return userGroups.stream()
                .map(userGroup -> userGroup.getUser().getId())
                .toList();
    }

    @Named("toAccount")
    default String convertToAccount(UserGroup userGroup) {
        return userGroup.getUser().getAccount();
    }

    default List<String> convertToClassAdminAccount(List<UserGroup> userGroups) {
        return userGroups.stream()
                .map(userGroup -> {
                    User user = userGroup.getUser();
                    for (UserRole role : user.getUserRoles()) {
                        if (role.getRole().getRoleName().contains("GROUP_ADMIN")) {
                            return user.getAccount();
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
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

    default Location mapLocation(Integer id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
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

    default TechnicalGroup mapTechnicalGroup(Integer id) {
        if (id == null) {
            return null;
        }
        TechnicalGroup technicalGroup = new TechnicalGroup();
        technicalGroup.setId(id);
        return technicalGroup;
    }
}
