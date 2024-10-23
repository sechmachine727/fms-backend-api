package org.fms.training.common.dto.trainerdto;

public record SaveTrainerDTO(
        Integer userId,
        String jobRank,
        String certificate,
        String phone,
        String professionalLevel,
        String trainerType,
        String contributionType,
        String note
) {
}
