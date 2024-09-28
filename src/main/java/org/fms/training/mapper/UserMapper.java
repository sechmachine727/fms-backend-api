package org.fms.training.mapper;

import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.Department;
import org.fms.training.entity.User;
import org.fms.training.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "department.id", target = "departmentId")
    SaveUserDTO toSaveUserDTO(User user);

    @Mapping(target = "department", source = "departmentId")
    User toUserEntity(SaveUserDTO saveUserDTO);

    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "userRoles", target = "roleNames")
    ReadUserDTO toReadUserDTO(User user);

    void updateUserFromDTO(SaveUserDTO saveUserDTO, @MappingTarget User user);

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