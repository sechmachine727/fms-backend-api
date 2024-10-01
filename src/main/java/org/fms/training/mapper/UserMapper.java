package org.fms.training.mapper;

import org.fms.training.dto.userdto.ClassAdminDTO;
import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.Department;
import org.fms.training.entity.User;
import org.fms.training.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "department.id", target = "departmentId")
    SaveUserDTO toSaveUserDTO(User user);

    @Mapping(target = "department", source = "departmentId")
    User toUserEntity(SaveUserDTO saveUserDTO);

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "userRoles", target = "roleIds", qualifiedByName = "toRoleId")
    @Mapping(source = "userRoles", target = "roleNames", qualifiedByName = "toRoleName")
    ReadUserDTO toReadUserDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account")
    ClassAdminDTO toClassAdminDTO(User user);

    void updateUserFromDTO(SaveUserDTO saveUserDTO, @MappingTarget User user);

    @Named("toRoleId")
    default Integer convertToRoleId(UserRole role) {
        return role.getRole().getId();
    }

    @Named("toRoleName")
    default String convertToRoleName(UserRole role) {
        return role.getRole().getRoleName();
    }

    default Department mapDepartment(Integer departmentId) {
        if (departmentId == null) {
            return null;
        }
        Department department = new Department();
        department.setId(departmentId);
        return department;
    }
}