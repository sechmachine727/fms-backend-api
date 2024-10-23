package org.fms.training.common.dto.trainingcalendardto;

import org.fms.training.common.dto.trainingcalendardto.external.GroupDTO;
import org.fms.training.common.dto.trainingcalendardto.external.LessonDTO;
import org.fms.training.common.dto.trainingcalendardto.external.TopicDTO;
import org.fms.training.common.dto.trainingcalendardto.external.TrainerDTO;

import java.util.List;

public record CalendarTopicDTO(
        Integer id,
        String startDate,
        String endDate,
        GroupDTO group,
        TopicDTO topic,
        TrainerDTO trainer,
        List<LessonDTO> lessons
) {
}
