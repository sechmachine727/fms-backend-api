package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.external.TopicDTO;
import org.fms.training.common.entity.Topic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicCalendarMapper {
    TopicDTO toTopicDTO(Topic topic);
}