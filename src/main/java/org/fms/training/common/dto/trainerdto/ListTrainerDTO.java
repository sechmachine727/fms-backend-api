package org.fms.training.common.dto.trainerdto;

import org.fms.training.common.enums.Status;

public record ListTrainerDTO(
        Integer id,
        String trainerAccount,
        String trainerEmployeeId,
        String trainerName,
        String trainerEmail,
        String phone,
        String trainerType,
        Status trainerStatus
) {
}
