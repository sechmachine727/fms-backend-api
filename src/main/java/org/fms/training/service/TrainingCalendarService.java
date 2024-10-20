package org.fms.training.service;

import org.fms.training.common.dto.trainingcalendardto.CalendarTopicDTO;
import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;

import java.util.List;

public interface TrainingCalendarService {
    List<CalendarTopicDTO> generateTrainingCalendar(GenerateCalendarRequest request);
}
