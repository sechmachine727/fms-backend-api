package org.fms.training.common.dto.trainerdto;

import org.fms.training.common.enums.Status;

public record ReadTrainerDTO(
        Integer id,
        String trainerAccount,
        String trainerEmployeeId,
        String trainerName,
        String trainerEmail,
        String contractType,
        String phone,
        String jobRank,
        String certificate,
        String professionalLevel,
        String trainerType,
        String contributionType,
        String note,
        Status trainerStatus
) {
}
