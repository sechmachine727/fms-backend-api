package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.common.entity.TechnicalGroup;
import org.fms.training.common.mapper.groupmapper.TechnicalGroupMapper;
import org.fms.training.repository.TechnicalGroupRepository;
import org.fms.training.service.TechnicalGroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TechnicalGroupServiceImpl implements TechnicalGroupService {

    private final TechnicalGroupRepository technicalGroupRepository;
    private final TechnicalGroupMapper technicalGroupMapper;

    @Override
    public Optional<List<ListTechnicalGroupDTO>> getAllTechnicalGroups() {
        List<TechnicalGroup> technicalGroups = technicalGroupRepository.findAll();
        List<ListTechnicalGroupDTO> listTechnicalGroupDTOs = technicalGroups.stream()
                .map(technicalGroupMapper::toListDTO)
                .toList();

        return Optional.of(listTechnicalGroupDTOs);
    }
}
