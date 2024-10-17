package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.departmentdto.DepartmentDTO;
import org.fms.training.common.mapper.DepartmentMapper;
import org.fms.training.repository.DepartmentRepository;
import org.fms.training.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentMapper.toDepartmentDTOs(departmentRepository.findAll());
    }
}
