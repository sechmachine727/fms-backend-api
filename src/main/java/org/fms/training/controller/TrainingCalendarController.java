package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainingcalendardto.CalendarTopicDTO;
import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;
import org.fms.training.common.dto.trainingcalendardto.external.TopicDTO;
import org.fms.training.common.dto.trainingcalendardto.external.TrainerDTO;
import org.fms.training.common.entity.SlotTimeSuggestion;
import org.fms.training.service.TrainingCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-calendar")
@RequiredArgsConstructor
public class TrainingCalendarController {
    private final TrainingCalendarService trainingCalendarService;

    @PostMapping("/generate")
    public ResponseEntity<List<CalendarTopicDTO>> generateTrainingCalendar(@RequestBody GenerateCalendarRequest request) {
        List<CalendarTopicDTO> calendarTopics = trainingCalendarService.generateTrainingCalendar(request);
        return ResponseEntity.ok(calendarTopics);
    }

    @GetMapping("/display/{groupId}")
    public ResponseEntity<List<CalendarTopicDTO>> displayTrainingCalendar(@PathVariable Integer groupId) {
        List<CalendarTopicDTO> calendarTopics = trainingCalendarService.displayTrainingCalendar(groupId);
        return ResponseEntity.ok(calendarTopics);
    }

    @GetMapping("/presets")
    public ResponseEntity<List<SlotTimeSuggestion>> getSlotTimeSuggestions() {
        List<SlotTimeSuggestion> slotTimeSuggestions = trainingCalendarService.getSlotTimeSuggestions();
        return ResponseEntity.ok(slotTimeSuggestions);
    }

    @GetMapping("/trainers")
    public ResponseEntity<List<TrainerDTO>> getTrainers() {
        List<TrainerDTO> trainers = trainingCalendarService.getTrainers();
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/topics/{groupId}")
    public ResponseEntity<List<TopicDTO>> getTopicsByGroup(@PathVariable Integer groupId) {
        List<TopicDTO> topics = trainingCalendarService.getTopicsByGroup(groupId);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<TopicDTO>> getAvailableTopics() {
        List<TopicDTO> topics = trainingCalendarService.getAvailableTopics();
        return ResponseEntity.ok(topics);
    }
}