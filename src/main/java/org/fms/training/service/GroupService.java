package org.fms.training.service;

import org.fms.training.common.dto.groupdto.ListGroupDTO;
import org.fms.training.common.dto.groupdto.ReadGroupDTO;
import org.fms.training.common.dto.groupdto.SaveGroupDTO;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    Optional<List<ListGroupDTO>> getAllGroups();

    Optional<ReadGroupDTO> getGroupById(Integer id);

    void createGroup(SaveGroupDTO saveGroupDTO);

    Optional<List<ListGroupDTO>> getAllGroupsByAuthenticatedGroupAdmin(Authentication authentication);

    Optional<List<ListGroupDTO>> getAllGroupsByAuthenticatedCreator(Authentication authentication);

    void existsByGroupName(String name);

    void existsByGroupCode(String code);
}
