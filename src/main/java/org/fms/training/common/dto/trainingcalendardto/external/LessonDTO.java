package org.fms.training.common.dto.trainingcalendardto.external;

public record LessonDTO(
        Integer id,
        String startDate,
        String endDate,
        String startTime,
        String endTime,
        SimpleUnitDTO unit
) {
}