package org.fms.training.common.mapper;

import org.fms.training.common.dto.unitdto.UnitDTO;
import org.fms.training.common.entity.Unit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UnitSectionMapper.class)
public interface UnitMapper {
    UnitDTO toDTO(Unit unit);
}