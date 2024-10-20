package org.fms.training.common.dto.trainingcalendardto;

import java.util.List;

public record GenerateCalendarRequest(
        Integer groupId,
        String actualStartDate,
        Integer slotTimeSuggestionId,
        List<TopicTrainer> topics
) {
}
