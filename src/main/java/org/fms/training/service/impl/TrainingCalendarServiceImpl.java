package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainingcalendardto.CalendarTopicDTO;
import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;
import org.fms.training.common.dto.trainingcalendardto.SlotTimeSettings;
import org.fms.training.common.dto.trainingcalendardto.external.TopicDTO;
import org.fms.training.common.dto.trainingcalendardto.external.TopicTrainer;
import org.fms.training.common.dto.trainingcalendardto.external.TrainerDTO;
import org.fms.training.common.entity.*;
import org.fms.training.common.enums.Status;
import org.fms.training.common.mapper.trainingcalendarmapper.CalendarTopicMapper;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.repository.*;
import org.fms.training.service.TrainingCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingCalendarServiceImpl implements TrainingCalendarService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingCalendarServiceImpl.class);
    private final CalendarTopicRepository calendarTopicRepository;
    private final HolidayRepository holidayRepository;
    private final SlotTimeSuggestionRepository slotTimeSuggestionRepository;
    private final TrainerRepository trainerRepository;
    private final TopicRepository topicRepository;
    private final GroupRepository groupRepository;
    private final CalendarTopicMapper calendarTopicMapper;

    @Override
    public List<CalendarTopicDTO> displayTrainingCalendar(Integer groupId) {
        Group group = getGroupById(groupId);
        List<CalendarTopic> calendarTopics = calendarTopicRepository.findAllByGroup(group);
        return calendarTopics.stream()
                .map(calendarTopicMapper::toCalendarTopicDTO)
                .toList();
    }

    @Override
    public List<SlotTimeSuggestion> getSlotTimeSuggestions() {
        return slotTimeSuggestionRepository.findAll();
    }

    @Override
    public List<TrainerDTO> getTrainers() {
        return trainerRepository.findAll().stream()
                .map(trainer -> new TrainerDTO(trainer.getId(), trainer.getUser().getName()))
                .toList();
    }

    @Override
    public List<TopicDTO> getTopicsByGroup(Integer groupId) {
        Group group = getGroupById(groupId);
        return topicRepository.findTopicsByGroupId(group.getId()).stream()
                .map(topic -> new TopicDTO(topic.getId(), topic.getTopicCode(), topic.getVersion()))
                .toList();
    }

    @Override
    public List<CalendarTopicDTO> generateTrainingCalendar(GenerateCalendarRequest request) {
        Group group = getGroupById(request.groupId());
        group.setActualStartDate(parseStartDate(request.actualStartDate()));

        SlotTimeSuggestion slotTimeSuggestion = getSlotTimeSuggestionById(request.slotTimeSuggestionId());
        SlotTimeSettings slotTimeSettings = parseSlotTimeSuggestions(slotTimeSuggestion.getSuggestionName());
        List<Holiday> holidays = holidayRepository.findAllByStatus(Status.ACTIVE);

        List<CalendarTopic> existingCalendarTopics = calendarTopicRepository.findAllByGroup(group);
        Map<Integer, CalendarTopic> existingCalendarTopicMap = existingCalendarTopics.stream()
                .collect(Collectors.toMap(ct -> ct.getTopic().getId(), ct -> ct));

        List<CalendarTopic> updatedCalendarTopics = new ArrayList<>();

        for (TopicTrainer topicTrainer : request.topics()) {
            Trainer trainer = getTrainerById(topicTrainer.trainerId());
            Topic topic = getTopicById(topicTrainer.topicId());

            CalendarTopic calendarTopic = existingCalendarTopicMap.getOrDefault(topic.getId(), new CalendarTopic());
            calendarTopic.setGroup(group);
            calendarTopic.setTopic(topic);
            calendarTopic.setTrainer(trainer);

            updateOrCreateLessons(calendarTopic, topic, slotTimeSettings, holidays);

            if (!existingCalendarTopicMap.containsKey(topic.getId())) {
                updatedCalendarTopics.add(calendarTopic);
            }
        }

        if (!updatedCalendarTopics.isEmpty()) {
            CalendarTopic lastCalendarTopic = updatedCalendarTopics.get(updatedCalendarTopics.size() - 1);
            LocalDate lastEndDate = lastCalendarTopic.getEndDate();
            group.setActualEndDate(lastEndDate.atStartOfDay());
        }

        calendarTopicRepository.saveAll(updatedCalendarTopics);
        existingCalendarTopics.forEach(topic -> {
            if (!updatedCalendarTopics.contains(topic)) {
                updatedCalendarTopics.add(topic);
            }
        });

        return updatedCalendarTopics.stream()
                .map(calendarTopicMapper::toCalendarTopicDTO)
                .toList();
    }

    private void updateOrCreateLessons(CalendarTopic calendarTopic, Topic topic, SlotTimeSettings slotTimeSettings, List<Holiday> holidays) {
        LocalDate currentDate = calendarTopic.getGroup().getActualStartDate().toLocalDate();
        int daysPerUnit = slotTimeSettings.slotType().equalsIgnoreCase("PartTime") ? 2 : 1;

        List<Lesson> existingLessons = Optional.ofNullable(calendarTopic.getLessons()).orElse(new ArrayList<>());
        Map<Integer, Lesson> existingLessonsMap = existingLessons.stream()
                .collect(Collectors.toMap(lesson -> lesson.getUnit().getId(), lesson -> lesson));

        for (int i = 0; i < topic.getUnits().size(); i++) {
            while (!slotTimeSettings.trainingDaysOfWeek().contains(currentDate.getDayOfWeek()) || isHoliday(currentDate, holidays)) {
                currentDate = currentDate.plusDays(1);
            }

            Lesson lesson = existingLessonsMap.getOrDefault(topic.getUnits().get(i).getId(), new Lesson());
            lesson.setCalendarTopic(calendarTopic);
            lesson.setUnit(topic.getUnits().get(i));
            lesson.setStartDate(currentDate);
            lesson.setStartTime(slotTimeSettings.startTime());
            lesson.setEndTime(slotTimeSettings.endTime());

            LocalDate endDate = currentDate;
            for (int d = 1; d < daysPerUnit; d++) {
                do {
                    endDate = endDate.plusDays(1);
                } while (!slotTimeSettings.trainingDaysOfWeek().contains(endDate.getDayOfWeek()) || isHoliday(endDate, holidays));
            }
            lesson.setEndDate(endDate);

            if (!existingLessonsMap.containsKey(topic.getUnits().get(i).getId())) {
                if (calendarTopic.getLessons() == null) {
                    calendarTopic.setLessons(new ArrayList<>());
                }
                calendarTopic.getLessons().add(lesson);
            }

            currentDate = endDate.plusDays(1);
        }

        calendarTopic.setStartDate(calendarTopic.getGroup().getActualStartDate().toLocalDate());
        calendarTopic.setEndDate(currentDate.minusDays(1));
    }

    private Group getGroupById(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any group matching with id provided."));
    }

    private LocalDateTime parseStartDate(String actualStartDate) {
        try {
            return LocalDateTime.parse(actualStartDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use ISO-8601 format.", e);
        }
    }

    private SlotTimeSuggestion getSlotTimeSuggestionById(Integer slotTimeSuggestionId) {
        return slotTimeSuggestionRepository.findById(slotTimeSuggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any slot time suggestion with id matching with id provided."));
    }

    private Trainer getTrainerById(Integer trainerId) {
        return trainerRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any trainer associated with id provided."));
    }

    private Topic getTopicById(Integer topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any topic associated with id provided."));
    }

    private boolean isHoliday(LocalDate date, List<Holiday> holidays) {
        return holidays.stream().anyMatch(holiday -> !holiday.getStatus().equals(Status.INACTIVE) &&
                (date.isEqual(holiday.getStartDate()) || (date.isAfter(holiday.getStartDate()) && date.isBefore(holiday.getEndDate()))));
    }

    private SlotTimeSettings parseSlotTimeSuggestions(String slotTimeSuggestions) {
        try {
            String[] parts = slotTimeSuggestions.split(";\\s+");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid slotTimeSuggestions format. Expected 3 parts separated by '; '.");
            }

            String slotType = parts[0];
            List<DayOfWeek> trainingDaysOfWeek = parseDaysOfWeek(parts[1]);

            String[] timeRange = parts[2].substring(1, parts[2].length() - 1).split("\\s*-\\s*");
            if (timeRange.length != 2) {
                throw new IllegalArgumentException("Invalid time range format. Expected 'StartTime - EndTime'.");
            }
            LocalTime startTime = LocalTime.parse(timeRange[0]);
            LocalTime endTime = LocalTime.parse(timeRange[1]);

            return new SlotTimeSettings(slotType, trainingDaysOfWeek, startTime, endTime);

        } catch (IllegalArgumentException e) {
            logger.error("Error parsing slotTimeSuggestions: {}", e.getMessage());
            throw e;
        }
    }

    private List<DayOfWeek> parseDaysOfWeek(String daysOfWeekString) {
        try {
            String days = daysOfWeekString.substring(1, daysOfWeekString.length() - 1).trim();

            if (days.isBlank()) {
                return new ArrayList<>();
            }

            return Arrays.stream(days.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .map(this::intToDayOfWeek)
                    .toList();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid days of week format.", e);
        }
    }

    private DayOfWeek intToDayOfWeek(int dayOfWeekInt) {
        if (dayOfWeekInt < 2 || dayOfWeekInt > 8) {
            throw new IllegalArgumentException("Day of week value should be from 2 - 8");
        }
        return DayOfWeek.of(dayOfWeekInt - 1);
    }
}