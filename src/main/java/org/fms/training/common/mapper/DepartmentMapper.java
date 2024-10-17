package org.fms.training.common.mapper;

import org.fms.training.common.dto.departmentdto.DepartmentDTO;
import org.fms.training.common.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "departmentName", target = "departmentName")
    DepartmentDTO toDepartmentDTO(Department department);

    List<DepartmentDTO> toDepartmentDTOs(List<Department> departments);
}
