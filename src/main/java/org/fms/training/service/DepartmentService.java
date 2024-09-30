package org.fms.training.service;

import org.fms.training.dto.departmentdto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
}
