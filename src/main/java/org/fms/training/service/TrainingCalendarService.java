package org.fms.training.service;

import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;
import org.fms.training.common.entity.CalendarTopic;

import java.util.List;

public interface TrainingCalendarService {
    List<CalendarTopic> generateTrainingCalendar(GenerateCalendarRequest request);
}
