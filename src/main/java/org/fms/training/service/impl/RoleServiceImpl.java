package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.userdto.RoleDTO;
import org.fms.training.common.entity.Role;
import org.fms.training.repository.RoleRepository;
import org.fms.training.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRoleName());
    }

}
