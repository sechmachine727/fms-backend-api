package org.fms.training.service;

import org.fms.training.common.dto.trainingcalendardto.CalendarTopicDTO;
import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;
import org.fms.training.common.dto.trainingcalendardto.external.TopicDTO;
import org.fms.training.common.dto.trainingcalendardto.external.TrainerDTO;
import org.fms.training.common.entity.SlotTimeSuggestion;

import java.util.List;

public interface TrainingCalendarService {
    List<CalendarTopicDTO> generateTrainingCalendar(GenerateCalendarRequest request);

    List<CalendarTopicDTO> displayTrainingCalendar(Integer groupId);

    List<SlotTimeSuggestion> getSlotTimeSuggestions();

    List<TrainerDTO> getTrainers();

    List<TopicDTO> getTopicsByGroup(Integer groupId);
}
