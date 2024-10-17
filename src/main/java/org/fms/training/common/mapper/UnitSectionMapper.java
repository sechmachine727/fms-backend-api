package org.fms.training.common.mapper;

import org.fms.training.common.dto.unitsectiondto.UnitSectionDTO;
import org.fms.training.common.entity.UnitSection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitSectionMapper {
    UnitSectionDTO toDTO(UnitSection unitSection);
}