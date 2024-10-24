package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.groupdto.ListGroupDTO;
import org.fms.training.common.dto.groupdto.ReadGroupDTO;
import org.fms.training.common.dto.groupdto.SaveGroupDTO;
import org.fms.training.common.entity.Group;
import org.fms.training.common.entity.User;
import org.fms.training.common.entity.UserGroup;
import org.fms.training.common.enums.GroupStatus;
import org.fms.training.common.mapper.groupmapper.GroupMapper;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.repository.GroupRepository;
import org.fms.training.repository.UserGroupRepository;
import org.fms.training.repository.UserRepository;
import org.fms.training.service.EmailService;
import org.fms.training.service.GroupService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public Optional<List<ListGroupDTO>> getAllGroups() {
        List<Group> groups = groupRepository.getAllByOrderByLastModifiedDateDesc();
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
        validFieldsCheck(saveGroupDTO);

        Group group = groupMapper.toGroupEntity(saveGroupDTO);

        // Save the group first to get its ID
        Group savedGroup = groupRepository.save(group);

        // Associate users with the group
        List<UserGroup> userGroups = saveGroupDTO.getAssignedUserIds().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    UserGroup userGroup = new UserGroup();
                    userGroup.setGroup(savedGroup);
                    userGroup.setUser(user);
                    return userGroup;
                })
                .toList();
        userGroupRepository.saveAll(userGroups);

        // If the group's status is "ASSIGNED", notify the users
        if (group.getStatus() == GroupStatus.ASSIGNED) {
            // Get all user's email addresses
            List<String> assignedUserEmails = saveGroupDTO.getAssignedUserIds().stream()
                    .map(userId -> {
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                        return user.getEmail();
                    })
                    .toList();

            // Send email notification to each user asynchronously
            assignedUserEmails.forEach(email -> {
                Map<String, Object> emailVariables = Map.of(
                        "groupName", group.getGroupName(),
                        "groupCode", group.getGroupCode()
                );

                // Call the async email service (no try-catch needed, exceptions handled in async method)
                emailService.sendHtmlEmail(email, "You have been assigned to a new group", "group-assigned-email", emailVariables);
            });
        }
    }

    @Override
    @Transactional
    public void updateGroup(Integer id, SaveGroupDTO saveGroupDTO) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group does not exist " + id));

        validFieldsCheck(group, saveGroupDTO);
        groupMapper.updateGroupEntity(saveGroupDTO, group);

        // Save the group first to get its ID
        Group savedGroup = groupRepository.save(group);

        // Fetch existing user associations
        List<UserGroup> existingUserGroups = userGroupRepository.findByGroupId(id);
        Map<Integer, UserGroup> existingUserMap = existingUserGroups.stream()
                .collect(Collectors.toMap(ug -> ug.getUser().getId(), ug -> ug));

        // Prepare new user associations
        List<Integer> newUserIds = saveGroupDTO.getAssignedUserIds();
        List<UserGroup> newUserGroups = newUserIds.stream()
                .filter(userId -> !existingUserMap.containsKey(userId))
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    UserGroup userGroup = new UserGroup();
                    userGroup.setGroup(savedGroup);
                    userGroup.setUser(user);
                    return userGroup;
                })
                .toList();

        // Remove users that are not in the request
        existingUserGroups.stream()
                .filter(ug -> !newUserIds.contains(ug.getUser().getId()))
                .forEach(userGroupRepository::delete);

        // Add new users that are in the request but not in the existing associations
        userGroupRepository.saveAll(newUserGroups);

        // If the group's status is "ASSIGNED", notify the new users
        if (group.getStatus() == GroupStatus.ASSIGNED) {
            // Get new user's email addresses
            List<String> newUserEmails = newUserGroups.stream()
                    .map(ug -> ug.getUser().getEmail())
                    .toList();

            // Send email notification to each new user asynchronously
            newUserEmails.forEach(email -> {
                Map<String, Object> emailVariables = Map.of(
                        "groupName", group.getGroupName(),
                        "groupCode", group.getGroupCode()
                );

                // Call the async email service (no try-catch needed, exceptions handled in async method)
                emailService.sendHtmlEmail(email, "You have been assigned to a new group", "group-assigned-email", emailVariables);
            });
        }
    }

    @Override
    public Optional<List<ListGroupDTO>> getAllGroupsByAuthenticatedGroupAdmin(Authentication authentication) {
        Optional<User> user = userRepository.findByAccount(authentication.getName());
        if (user.isPresent()) {  // Simplify null check
            List<UserGroup> userGroups = userGroupRepository.findByUserIdOrderByGroupLastModifiedDateDesc(user.get().getId());
            return Optional.of(userGroups.stream()
                    .map(userGroup -> groupMapper.toListGroupDTO(userGroup.getGroup()))
                    .toList());
        } else {
            throw new ResourceNotFoundException("User not found");  // throw this in else to keep similar logic
        }
    }

    @Override
    public Optional<List<ListGroupDTO>> getAllGroupsByAuthenticatedCreator(Authentication authentication) {
        Optional<User> user = userRepository.findByAccount(authentication.getName());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        List<Group> groups = groupRepository.findByCreatedByOrderByLastModifiedDateDesc(user.get().getAccount());
        return Optional.of(groups.stream()
                .map(groupMapper::toListGroupDTO)
                .toList());
    }

    private void validFieldsCheck(SaveGroupDTO saveGroupDTO) {
        Map<String, String> errors = new HashMap<>();

        if (groupRepository.existsByGroupCode(saveGroupDTO.getGroupCode())) {
            errors.put("groupCode", "Group code already exists.");
        }

        // Parse and validate dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime startDate;
        LocalDateTime endDate;

        try {
            startDate = LocalDateTime.parse(saveGroupDTO.getExpectedStartDate(), formatter);
            endDate = LocalDateTime.parse(saveGroupDTO.getExpectedEndDate(), formatter);
        } catch (DateTimeParseException e) {
            errors.put("dateFormat", "Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss.SSS.");
            throw new ValidationException(errors);
        }

        // Validate start date is not after end date
        if (startDate.isAfter(endDate)) {
            errors.put("dateOrder", "Start date cannot be after end date.");
        }

        // Validate end date is not in the past
        if (endDate.isBefore(LocalDateTime.now())) {
            errors.put("datePast", "End date cannot be in the past.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validFieldsCheck(Group group, SaveGroupDTO saveGroupDTO) {
        Map<String, String> errors = new HashMap<>();

        if (!group.getGroupCode().equals(saveGroupDTO.getGroupCode()) &&
                groupRepository.existsByGroupCode(saveGroupDTO.getGroupCode())) {
            errors.put("groupCode", "Group code already exists.");
        }

        // Parse and validate dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime startDate;
        LocalDateTime endDate;

        try {
            startDate = LocalDateTime.parse(saveGroupDTO.getExpectedStartDate(), formatter);
            endDate = LocalDateTime.parse(saveGroupDTO.getExpectedEndDate(), formatter);
        } catch (DateTimeParseException e) {
            errors.put("dateFormat", "Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss.SSS.");
            throw new ValidationException(errors);
        }

        // Validate start date is not after end date
        if (startDate.isAfter(endDate)) {
            errors.put("dateOrder", "Start date cannot be after end date.");
        }

        // Validate end date is not in the past
        if (endDate.isBefore(LocalDateTime.now())) {
            errors.put("datePast", "End date cannot be in the past.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void existsByGroupName(String name) {
        groupRepository.findByGroupName(name);
    }

    @Override
    public void existsByGroupCode(String code) {
        groupRepository.findByGroupCode(code);
    }
}