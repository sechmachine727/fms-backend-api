package org.fms.training.service;

import org.fms.training.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> findAll();
}
