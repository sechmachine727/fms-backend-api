package org.fms.training.common.mapper.groupmapper;

import org.fms.training.common.dto.groupdto.ListGroupDTO;
import org.fms.training.common.dto.groupdto.ReadGroupDTO;
import org.fms.training.common.dto.groupdto.SaveGroupDTO;
import org.fms.training.common.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GroupTrainingProgramMapper.class, AssignedUserMapper.class})
public interface GroupMapper {
    @Mapping(source = "expectedStartDate", target = "expectedStartDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "expectedEndDate", target = "expectedEndDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "actualStartDate", target = "actualStartDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "actualEndDate", target = "actualEndDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "trainingProgram", target = "trainingProgram")
    @Mapping(source = "userGroups", target = "assignedUsers")
    ReadGroupDTO toReadGroupDTO(Group group);

    @Mapping(source = "trainingProgram.id", target = "trainingProgramId")
    @Mapping(source = "trainingProgram.trainingProgramName", target = "trainingProgramName")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "traineeType.traineeTypeName", target = "traineeTypeName")
    @Mapping(source = "site.siteName", target = "siteName")
    @Mapping(source = "location.locationName", target = "locationName")
    @Mapping(source = "expectedStartDate", target = "expectedStartDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "expectedEndDate", target = "expectedEndDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "actualStartDate", target = "actualStartDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "actualEndDate", target = "actualEndDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "userGroups", target = "assignedUsers")
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
    @Mapping(target = "expectedStartDate", source = "expectedStartDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Mapping(target = "expectedEndDate", source = "expectedEndDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    Group toGroupEntity(SaveGroupDTO saveGroupDTO);

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
