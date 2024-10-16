package org.fms.training.common.mapper;

import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.common.entity.TechnicalGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface TechnicalGroupMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "code")
    ListTechnicalGroupDTO toListDTO(TechnicalGroup technicalGroup);
}
