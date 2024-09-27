package org.fms.training.mapper;

import org.fms.training.dto.unitdto.UnitDTO;
import org.fms.training.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UnitSectionMapper.class)
public interface UnitMapper {
    @Mapping(source = "unitName", target = "unitName")
    UnitDTO toDTO(Unit unit);
}