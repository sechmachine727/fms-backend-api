package org.fms.training.common.mapper.groupmapper;

import org.fms.training.common.dto.groupdto.external.AssignedUserDTO;
import org.fms.training.common.entity.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignedUserMapper {
    @Mapping(source = "userGroup.user.id", target = "id")
    @Mapping(source = "userGroup.user.account", target = "account")
    AssignedUserDTO toAssignedUserDTO(UserGroup userGroup);
}
