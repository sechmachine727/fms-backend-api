package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.external.LessonDTO;
import org.fms.training.common.entity.Lesson;
import org.fms.training.common.mapper.UnitMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UnitMapper.class})
public interface LessonMapper {
    @Mapping(source = "startDate", target = "startDate", dateFormat = "dd-MM-YYYY")
    @Mapping(source = "endDate", target = "endDate", dateFormat = "dd-MM-YYYY")
    LessonDTO toLessonDTO(Lesson lesson);
}
