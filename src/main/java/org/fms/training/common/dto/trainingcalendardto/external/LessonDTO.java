package org.fms.training.common.dto.trainingcalendardto.external;

import java.time.LocalDate;
import java.time.LocalTime;

public record LessonDTO(
        Integer id,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime startTime,
        LocalTime endTime,
        String unitName
) {
}