package org.fms.training.service.impl;

import org.fms.training.mapper.DepartmentMapper;
import org.fms.training.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDepartments_shouldReturnListOfDepartments() {
        // when
        departmentService.getAllDepartments();

        // then
        then(departmentRepository).should(times(1)).findAll();
        then(departmentMapper).should(times(1)).toDepartmentDTOs(List.of());
    }
}
