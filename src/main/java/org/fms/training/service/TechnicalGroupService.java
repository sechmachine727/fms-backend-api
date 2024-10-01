package org.fms.training.service;

import org.fms.training.dto.technicalgroupdto.ListTechnicalGroupDTO;

import java.util.List;
import java.util.Optional;

public interface TechnicalGroupService {
    Optional<List<ListTechnicalGroupDTO>> getAllTechnicalGroups();
}
