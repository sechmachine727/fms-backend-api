package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.userdto.RoleDTO;
import org.fms.training.entity.Role;
import org.fms.training.repository.RoleRepository;
import org.fms.training.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRoleName());
    }

}
