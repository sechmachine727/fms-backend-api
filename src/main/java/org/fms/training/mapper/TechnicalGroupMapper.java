package org.fms.training.mapper;

import org.fms.training.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.entity.TechnicalGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface TechnicalGroupMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "code")
    ListTechnicalGroupDTO toListDTO(TechnicalGroup technicalGroup);
}
