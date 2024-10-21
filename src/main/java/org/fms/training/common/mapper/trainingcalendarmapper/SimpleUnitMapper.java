package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.external.SimpleUnitDTO;
import org.fms.training.common.entity.Unit;
import org.fms.training.common.mapper.UnitSectionMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UnitSectionMapper.class})
public interface SimpleUnitMapper {
    SimpleUnitDTO toSimpleUnitDTO(Unit unit);
}
