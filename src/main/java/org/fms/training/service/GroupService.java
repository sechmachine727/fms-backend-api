package org.fms.training.service;

import org.fms.training.common.dto.groupdto.ListGroupDTO;
import org.fms.training.common.dto.groupdto.ReadGroupDTO;
import org.fms.training.common.dto.groupdto.SaveGroupDTO;
import org.fms.training.common.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Optional<List<ListGroupDTO>> getAllGroups(String search);

    Optional<ReadGroupDTO> getGroupById(Integer id);

    void createGroup(SaveGroupDTO saveGroupDTO);

    Group existsByGroupName(String name);

    Group existsByGroupCode(String code);
}
