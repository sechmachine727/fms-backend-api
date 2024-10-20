package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainingcalendardto.CalendarTopicDTO;
import org.fms.training.common.dto.trainingcalendardto.GenerateCalendarRequest;
import org.fms.training.common.dto.trainingcalendardto.SlotTimeSettings;
import org.fms.training.common.dto.trainingcalendardto.external.TopicTrainer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<CalendarTopicDTO> generateTrainingCalendar(GenerateCalendarRequest request) {
        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any group matching with id provided."));

        try {
            LocalDateTime parsedStartDate = LocalDateTime.parse(request.actualStartDate());
            group.setActualStartDate(parsedStartDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use ISO-8601 format.", e);
        }

        SlotTimeSuggestion slotTimeSuggestion = slotTimeSuggestionRepository.findById(request.slotTimeSuggestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find any slot time suggestion with id matching with id provided."));
        String slotTimeSuggestions = slotTimeSuggestion.getSuggestionName();

        SlotTimeSettings slotTimeSettings = parseSlotTimeSuggestions(slotTimeSuggestions);
        List<Holiday> holidays = holidayRepository.findAllByStatus(Status.ACTIVE);

        deactivateOldCalendarTopics(group);

        List<CalendarTopic> calendarTopics = new ArrayList<>();
        LocalDate currentDate = group.getActualStartDate().toLocalDate();

        for (TopicTrainer topicTrainer : request.topics()) {
            Trainer trainer = trainerRepository.findById(topicTrainer.trainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could not find any trainer associated with id provided."));
            Topic topic = topicRepository.findById(topicTrainer.topicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Could not find any topic associated with id provided."));

            CalendarTopic calendarTopic = new CalendarTopic();
            calendarTopic.setGroup(group);
            calendarTopic.setTopic(topic);
            calendarTopic.setTrainer(trainer);
            calendarTopic.setStatus(Status.ACTIVE);

            LocalDate topicStartDate = currentDate;
            int daysPerUnit = slotTimeSettings.slotType().equalsIgnoreCase("PartTime") ? 2 : 1;

            for (int i = 0; i < topic.getUnits().size(); i++) {
                while (!slotTimeSettings.trainingDaysOfWeek().contains(currentDate.getDayOfWeek()) || isHoliday(currentDate, holidays)) {
                    currentDate = currentDate.plusDays(1);
                }

                Lesson lesson = new Lesson();
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

                if (calendarTopic.getLessons() == null) {
                    calendarTopic.setLessons(new ArrayList<>());
                }
                calendarTopic.getLessons().add(lesson);

                currentDate = endDate.plusDays(1);
            }
            calendarTopic.setStartDate(topicStartDate);
            calendarTopic.setEndDate(currentDate.minusDays(1));

            calendarTopics.add(calendarTopic);
        }

        if (!calendarTopics.isEmpty()) {
            CalendarTopic lastCalendarTopic = calendarTopics.get(calendarTopics.size() - 1);
            LocalDate lastEndDate = lastCalendarTopic.getEndDate();
            group.setActualEndDate(lastEndDate.atStartOfDay());
        }
        calendarTopicRepository.saveAll(calendarTopics);

        return calendarTopics.stream()
                .map(calendarTopicMapper::toCalendarTopicDTO)
                .toList();
    }


    private void deactivateOldCalendarTopics(Group group) {
        List<CalendarTopic> oldCalendarTopics = calendarTopicRepository.findAllByGroupAndStatus(group, Status.ACTIVE);
        oldCalendarTopics.forEach(ct -> ct.setStatus(Status.INACTIVE));
        calendarTopicRepository.saveAll(oldCalendarTopics);
    }

    private boolean isHoliday(LocalDate date, List<Holiday> holidays) {
        return holidays.stream().anyMatch(holiday -> !holiday.getStatus().equals(Status.INACTIVE) && (date.isEqual(holiday.getStartDate()) || (date.isAfter(holiday.getStartDate()) && date.isBefore(holiday.getEndDate()))));
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
            throw e; // Re-throw for the calling method to handle
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