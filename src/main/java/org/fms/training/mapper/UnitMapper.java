package org.fms.training.mapper;

import org.fms.training.dto.unitdto.UnitDTO;
import org.fms.training.entity.Unit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UnitSectionMapper.class)
public interface UnitMapper {
    UnitDTO toDTO(Unit unit);
}