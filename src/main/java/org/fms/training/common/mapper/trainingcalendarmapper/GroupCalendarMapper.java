package org.fms.training.common.mapper.trainingcalendarmapper;

import org.fms.training.common.dto.trainingcalendardto.external.GroupDTO;
import org.fms.training.common.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupCalendarMapper {
    GroupDTO toGroupDTO(Group group);
}