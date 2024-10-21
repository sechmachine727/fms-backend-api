package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.external.LessonDTO;
import org.fms.training.common.entity.Lesson;
import org.fms.training.common.mapper.UnitMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UnitMapper.class})
public interface LessonMapper {
    LessonDTO toLessonDTO(Lesson lesson);
}
