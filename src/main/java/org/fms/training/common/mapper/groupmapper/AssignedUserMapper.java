package org.fms.training.common.mapper.groupmapper;

import org.fms.training.common.dto.groupdto.external.AssignedUserDTO;
import org.fms.training.common.entity.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignedUserMapper {
    @Mapping(source = "userGroup.user.id", target = "userId")
    @Mapping(source = "userGroup.user.account", target = "userAccount")
    AssignedUserDTO toAssignedUserDTO(UserGroup userGroup);
}
