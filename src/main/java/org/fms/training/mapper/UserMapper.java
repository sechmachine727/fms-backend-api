package org.fms.training.mapper;

import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "account", target = "account")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "employeeId", target = "employeeId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "contactType", target = "contactType")
    @Mapping(source = "department", target = "department")
    @Mapping(target = "roles", ignore = true)
    SaveUserDTO toSaveUserDTO(User user);

    User toUserEntity(SaveUserDTO saveUserDTO);

    void updateUserFromDTO(SaveUserDTO saveUserDTO, @MappingTarget User user);
}
