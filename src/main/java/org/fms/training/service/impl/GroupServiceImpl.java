package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.groupdto.ListGroupDTO;
import org.fms.training.dto.groupdto.ReadGroupDTO;
import org.fms.training.dto.groupdto.SaveGroupDTO;
import org.fms.training.entity.Group;
import org.fms.training.entity.User;
import org.fms.training.entity.UserGroup;
import org.fms.training.mapper.GroupMapper;
import org.fms.training.repository.GroupRepository;
import org.fms.training.repository.UserGroupRepository;
import org.fms.training.repository.UserRepository;
import org.fms.training.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<List<ListGroupDTO>> getAllGroups(String search) {
        List<Group> groups = groupRepository.findByGroupNameContainingIgnoreCaseAndGroupCodeContainingIgnoreCase(search);
        return Optional.of(groups.stream()
                .map(groupMapper::toListGroupDTO)
                .toList());
    }

    @Override
    public Optional<ReadGroupDTO> getGroupById(Integer id) {
        return groupRepository.findById(id)
                .map(groupMapper::toReadGroupDTO);
    }

    @Override
    @Transactional
    public void createGroup(SaveGroupDTO saveGroupDTO) {
        Group group = groupMapper.toGroupEntity(saveGroupDTO);

        // Save the group first to get its ID
        Group savedGroup = groupRepository.save(group);

        // Associate users with the group
        List<UserGroup> userGroups = saveGroupDTO.getAssignedUserIds().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    UserGroup userGroup = new UserGroup();
                    userGroup.setGroup(savedGroup);
                    userGroup.setUser(user);
                    return userGroup;
                })
                .toList();
        userGroupRepository.saveAll(userGroups);
    }
}
