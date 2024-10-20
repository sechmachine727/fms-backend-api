package org.fms.training.common.dto.trainingcalendardto;

import org.fms.training.common.dto.trainingcalendardto.external.TopicTrainer;

import java.util.List;

public record GenerateCalendarRequest(
        Integer groupId,
        String actualStartDate,
        Integer slotTimeSuggestionId,
        List<TopicTrainer> topics
) {
}
