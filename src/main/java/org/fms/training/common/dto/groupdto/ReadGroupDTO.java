package org.fms.training.common.dto.groupdto;

import org.fms.training.common.dto.groupdto.external.AssignedUserDTO;
import org.fms.training.common.dto.groupdto.external.GroupTrainingProgramDTO;
import org.fms.training.common.entity.*;
import org.fms.training.common.enums.GroupStatus;

import java.util.List;

public record ReadGroupDTO(
        Integer id,
        String groupName,
        String groupCode,
        Integer traineeNumber,
        GroupTrainingProgramDTO trainingProgram,
        TechnicalGroup technicalGroup,
        Site site,
        Location location,
        String expectedStartDate,
        String expectedEndDate,
        String actualStartDate,
        String actualEndDate,
        String note,
        DeliveryType deliveryType,
        TraineeType traineeType,
        Scope scope,
        String createdBy,
        FormatType formatType,
        KeyProgram keyProgram,
        GroupStatus status,
        List<AssignedUserDTO> assignedUsers
) {
}
