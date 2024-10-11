package org.fms.training.service.impl;

import org.fms.training.dto.userdto.RoleDTO;
import org.fms.training.entity.Role;
import org.fms.training.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoles_shouldReturnRoleDTOs_whenRolesExist() {
        // given
        Role role1 = new Role(1L, "ADMIN");
        Role role2 = new Role(2L, "USER");
        List<Role> roles = List.of(role1, role2);
        given(roleRepository.findAll()).willReturn(roles);

        // when
        List<RoleDTO> result = roleService.getAllRoles();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRoleName()).isEqualTo("ADMIN");
        assertThat(result.get(1).getRoleName()).isEqualTo("USER");

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getAllRoles_shouldReturnEmptyList_whenNoRolesExist() {
        // given
        given(roleRepository.findAll()).willReturn(List.of());

        // when
        List<RoleDTO> result = roleService.getAllRoles();

        // then
        assertThat(result).isEmpty();
        verify(roleRepository, times(1)).findAll();
    }
}
