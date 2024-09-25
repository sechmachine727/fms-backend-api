package org.fms.training.mapper;

import org.fms.training.dto.unitsectiondto.UnitSectionDTO;
import org.fms.training.entity.UnitSection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitSectionMapper {
    UnitSectionDTO toDTO(UnitSection unitSection);
}