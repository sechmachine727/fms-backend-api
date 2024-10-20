package org.fms.training.common.dto.trainingcalendardto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record SlotTimeSettings(
        String slotType,
        List<DayOfWeek> trainingDaysOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}

