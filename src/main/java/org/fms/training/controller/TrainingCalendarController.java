package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;
import org.fms.training.common.entity.CalendarTopic;
import org.fms.training.service.TrainingCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/training-calendar")
@RequiredArgsConstructor
public class TrainingCalendarController {
    private final TrainingCalendarService trainingCalendarService;

    @PostMapping("/generate")
    public ResponseEntity<List<CalendarTopic>> generateTrainingCalendar(@RequestBody GenerateCalendarRequest request) {
        List<CalendarTopic> calendarTopics = trainingCalendarService.generateTrainingCalendar(request);
        return ResponseEntity.ok(calendarTopics);
    }
}