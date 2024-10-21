package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.CalendarTopicDTO;
import org.fms.training.common.entity.CalendarTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface CalendarTopicMapper {
    @Mapping(source = "group.groupName", target = "group.groupName")
    @Mapping(source = "trainer.user.name", target = "trainer.name")
    @Mapping(source = "topic.topicName", target = "topic.topicName")
    CalendarTopicDTO toCalendarTopicDTO(CalendarTopic calendarTopic);
}