package org.fms.training.common.mapper;

import org.fms.training.common.dto.departmentdto.DepartmentDTO;
import org.fms.training.common.dto.userdto.ClassAdminDTO;
import org.fms.training.common.dto.userdto.ReadUserDTO;
import org.fms.training.common.dto.userdto.RoleDTO;
import org.fms.training.common.dto.userdto.SaveUserDTO;
import org.fms.training.common.entity.Department;
import org.fms.training.common.entity.User;
import org.fms.training.common.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "department.id", target = "departmentId")
    SaveUserDTO toSaveUserDTO(User user);

    @Mapping(target = "department", source = "departmentId")
    User toUserEntity(SaveUserDTO saveUserDTO);

    @Mapping(source = "department", target = "department")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "userRoles", target = "roles")
    ReadUserDTO toReadUserDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account")
    ClassAdminDTO toClassAdminDTO(User user);

    @Mapping(target = "department", source = "departmentId")
    void updateUserFromDTO(SaveUserDTO saveUserDTO, @MappingTarget User user);

    default List<RoleDTO> mapRoles(List<UserRole> userRoles) {
        return userRoles.stream()
                .map(role -> new RoleDTO(role.getRole().getId(), role.getRole().getRoleName()))
                .toList();
    }

    default Department mapDepartment(Integer departmentId) {
        if (departmentId == null) {
            return null;
        }
        Department department = new Department();
        department.setId(departmentId);
        return department;
    }

    default DepartmentDTO mapDepartment(Department department) {
        if (department == null) return null;
        return new DepartmentDTO(department.getId(), department.getDepartmentName());
    }
}