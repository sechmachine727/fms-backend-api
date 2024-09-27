package org.fms.training.mapper;

import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.User;
import org.fms.training.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

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

    @Mapping(source = "userRoles", target = "roleNames")
    ReadUserDTO toReadUserDTO(User user);

    void updateUserFromDTO(SaveUserDTO saveUserDTO, @MappingTarget User user);

    default String convertToRoleName(UserRole role) {
        return role.getRole().getRoleName();
    }

    default List<String> convertToRoleNameList(List<UserRole> roles) {
        return roles.stream()
                .map(this::convertToRoleName)
                .collect(Collectors.toList());
    }
}
