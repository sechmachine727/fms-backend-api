package org.fms.training.service.impl;

import org.fms.training.common.dto.groupdto.SaveGroupDTO;
import org.fms.training.common.entity.Group;
import org.fms.training.common.entity.User;
import org.fms.training.common.enums.GroupStatus;
import org.fms.training.common.mapper.GroupMapper;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.repository.GroupRepository;
import org.fms.training.repository.UserGroupRepository;
import org.fms.training.repository.UserRepository;
import org.fms.training.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGroups_shouldReturnListOfGroups() {
        // given
        given(groupRepository.getAllByOrderByLastModifiedDateDesc())
                .willReturn(Collections.emptyList());

        // when
        groupService.getAllGroups();

        // then
        then(groupRepository).should(times(1))
                .getAllByOrderByLastModifiedDateDesc();
    }

    @Test
    void getGroupById_withExistentGroup_shouldReturnGroup() {
        // given
        Integer groupId = 1;
        Group group = new Group();
        given(groupRepository.findById(groupId)).willReturn(Optional.of(group));

        // when
        groupService.getGroupById(groupId);

        // then
        then(groupRepository).should(times(1)).findById(groupId);
    }

    @Test
    void createGroup_withValidData_shouldSaveGroup() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setGroupName("Java Group");
        saveGroupDTO.setGroupCode("G001");
        saveGroupDTO.setTraineeNumber(10);
        saveGroupDTO.setTrainingProgramId(1);
        saveGroupDTO.setTechnicalGroupId(1);
        saveGroupDTO.setSiteId(1);
        saveGroupDTO.setLocationId(1);
        saveGroupDTO.setDeliveryTypeId(1);
        saveGroupDTO.setTraineeTypeId(1);
        saveGroupDTO.setScopeId(1);
        saveGroupDTO.setFormatTypeId(1);
        saveGroupDTO.setKeyProgramId(1);
        saveGroupDTO.setStatus(GroupStatus.PLANNING);
        saveGroupDTO.setAssignedUserIds(List.of(1, 2, 3));
        saveGroupDTO.setExpectedStartDate("2024-10-12T10:53:47.000");
        saveGroupDTO.setExpectedEndDate("2025-06-12T10:53:47.000");

        Group group = new Group();
        given(groupMapper.toGroupEntity(saveGroupDTO)).willReturn(group);
        given(groupRepository.save(group)).willReturn(group);

        User user = new User();
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));

        // when
        groupService.createGroup(saveGroupDTO);

        // then
        then(groupRepository).should(times(1)).save(group);
        then(userGroupRepository).should(times(1)).saveAll(anyList());
    }

    @Test
    void createGroup_withDuplicateGroupCode_shouldThrowValidationException() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setGroupName("Java Group");
        saveGroupDTO.setGroupCode("G001");
        saveGroupDTO.setTraineeNumber(10);
        saveGroupDTO.setTrainingProgramId(1);
        saveGroupDTO.setTechnicalGroupId(1);
        saveGroupDTO.setSiteId(1);
        saveGroupDTO.setLocationId(1);
        saveGroupDTO.setDeliveryTypeId(1);
        saveGroupDTO.setTraineeTypeId(1);
        saveGroupDTO.setScopeId(1);
        saveGroupDTO.setFormatTypeId(1);
        saveGroupDTO.setKeyProgramId(1);
        saveGroupDTO.setStatus(GroupStatus.PLANNING);
        saveGroupDTO.setAssignedUserIds(List.of(1, 2, 3));
        saveGroupDTO.setExpectedStartDate("2024-10-12T10:53:47.000");
        saveGroupDTO.setExpectedEndDate("2025-06-12T10:53:47.000");

        given(groupRepository.existsByGroupCode(saveGroupDTO.getGroupCode())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> groupService.createGroup(saveGroupDTO));
    }

    @Test
    void createGroup_withInvalidDateFormat_shouldThrowValidationException() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setExpectedStartDate("23-Oct-2024");
        saveGroupDTO.setExpectedEndDate("2024-11-22");

        // when, then
        assertThrows(ValidationException.class, () -> groupService.createGroup(saveGroupDTO));
    }

    @Test
    void createGroup_withEndDateBeforeStartDate_shouldThrowValidationException() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setExpectedStartDate("2026-10-12T10:53:47.000");
        saveGroupDTO.setExpectedEndDate("2025-06-12T10:53:47.000");

        // when, then
        assertThrows(ValidationException.class, () -> groupService.createGroup(saveGroupDTO));
    }

    @Test
    void createGroup_withEndDateInThePast_shouldThrowValidationException() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setExpectedStartDate("2022-10-12T10:53:47.000");
        saveGroupDTO.setExpectedEndDate("2023-06-12T10:53:47.000");

        // when, then
        assertThrows(ValidationException.class, () -> groupService.createGroup(saveGroupDTO));
    }

    @Test
    void createGroup_withNonExistentUser_shouldThrowResourceNotFoundException() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setGroupName("Java Group");
        saveGroupDTO.setGroupCode("G001");
        saveGroupDTO.setTraineeNumber(10);
        saveGroupDTO.setTrainingProgramId(1);
        saveGroupDTO.setTechnicalGroupId(1);
        saveGroupDTO.setSiteId(1);
        saveGroupDTO.setLocationId(1);
        saveGroupDTO.setDeliveryTypeId(1);
        saveGroupDTO.setTraineeTypeId(1);
        saveGroupDTO.setScopeId(1);
        saveGroupDTO.setFormatTypeId(1);
        saveGroupDTO.setKeyProgramId(1);
        saveGroupDTO.setStatus(GroupStatus.PLANNING);
        saveGroupDTO.setAssignedUserIds(List.of(1, 2, 3));
        saveGroupDTO.setExpectedStartDate("2024-10-12T10:53:47.000");
        saveGroupDTO.setExpectedEndDate("2025-06-12T10:53:47.000");

        given(userRepository.findById(anyInt())).willReturn(Optional.empty());

        // when, then
        assertThrows(ResourceNotFoundException.class, () -> groupService.createGroup(saveGroupDTO));
    }

    @Test
    void existsByGroupName_withExistentGroup_shouldReturnGroup() {
        // given
        String groupName = "Java Group";
        Group group = new Group();
        given(groupRepository.findByGroupName(groupName)).willReturn(Optional.of(group));

        // when
        groupService.existsByGroupName(groupName);

        // then
        then(groupRepository).should(times(1)).findByGroupName(groupName);
    }

    @Test
    void existsByGroupCode_withExistentGroup_shouldReturnGroup() {
        // given
        String groupCode = "G001";
        Group group = new Group();
        given(groupRepository.findByGroupCode(groupCode)).willReturn(Optional.of(group));

        // when
        groupService.existsByGroupCode(groupCode);

        // then
        then(groupRepository).should(times(1)).findByGroupCode(groupCode);
    }

    @Test
    void createGroup_withAssignedStatus_shouldSendEmailToUsersThatExists() {
        // given
        SaveGroupDTO saveGroupDTO = new SaveGroupDTO();
        saveGroupDTO.setGroupName("Java Group");
        saveGroupDTO.setGroupCode("G001");
        saveGroupDTO.setTraineeNumber(10);
        saveGroupDTO.setTrainingProgramId(1);
        saveGroupDTO.setTechnicalGroupId(1);
        saveGroupDTO.setSiteId(1);
        saveGroupDTO.setLocationId(1);
        saveGroupDTO.setDeliveryTypeId(1);
        saveGroupDTO.setTraineeTypeId(1);
        saveGroupDTO.setScopeId(1);
        saveGroupDTO.setFormatTypeId(1);
        saveGroupDTO.setKeyProgramId(1);
        saveGroupDTO.setStatus(GroupStatus.ASSIGNED);
        saveGroupDTO.setAssignedUserIds(List.of(1, 2));
        saveGroupDTO.setExpectedStartDate("2024-10-12T10:53:47.000");
        saveGroupDTO.setExpectedEndDate("2025-06-12T10:53:47.000");

        Group group = new Group();
        group.setGroupName("Java Group");
        group.setGroupCode("G001");
        group.setStatus(GroupStatus.ASSIGNED);
        given(groupMapper.toGroupEntity(saveGroupDTO)).willReturn(group);
        given(groupRepository.save(group)).willReturn(group);

        User user1 = new User();
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setEmail("user2@example.com");
        given(userRepository.findById(1)).willReturn(Optional.of(user1));
        given(userRepository.findById(2)).willReturn(Optional.of(user2));

        // when
        groupService.createGroup(saveGroupDTO);

        // then
        Map<String, Object> emailVariables = Map.of(
                "groupName", saveGroupDTO.getGroupName(),
                "groupCode", saveGroupDTO.getGroupCode()
        );
        then(emailService).should(times(1)).sendHtmlEmail("user1@example.com", "You have been assigned to a new group", "group-assigned-email", emailVariables);
        then(emailService).should(times(1)).sendHtmlEmail("user2@example.com", "You have been assigned to a new group", "group-assigned-email", emailVariables);
    }
}