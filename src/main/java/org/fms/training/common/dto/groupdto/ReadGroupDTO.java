package org.fms.training.common.dto.groupdto;

import org.fms.training.common.dto.deliverytypedto.DeliveryTypeDTO;
import org.fms.training.common.dto.groupdto.external.AssignedUserDTO;
import org.fms.training.common.dto.groupdto.external.GroupTrainingProgramDTO;
import org.fms.training.common.dto.locationdto.LocationDTO;
import org.fms.training.common.dto.sitedto.SiteDTO;
import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.common.entity.FormatType;
import org.fms.training.common.entity.KeyProgram;
import org.fms.training.common.entity.Scope;
import org.fms.training.common.entity.TraineeType;
import org.fms.training.common.enums.GroupStatus;

import java.util.List;

public record ReadGroupDTO(
        Integer id,
        String groupName,
        String groupCode,
        Integer traineeNumber,
        GroupTrainingProgramDTO trainingProgram,
        ListTechnicalGroupDTO technicalGroup,
        SiteDTO site,
        LocationDTO location,
        DeliveryTypeDTO deliveryType,
        TraineeType traineeType,
        Scope scope,
        String createdBy,
        FormatType formatType,
        KeyProgram keyProgram,
        GroupStatus status,
        List<AssignedUserDTO> assignedUsers,
        String expectedStartDate,
        String expectedEndDate,
        String actualStartDate,
        String actualEndDate,
        String note
) {
}
