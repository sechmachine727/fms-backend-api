package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.external.LessonDTO;
import org.fms.training.common.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(source = "unit.unitName", target = "unitName")
    LessonDTO toLessonDTO(Lesson lesson);
}
