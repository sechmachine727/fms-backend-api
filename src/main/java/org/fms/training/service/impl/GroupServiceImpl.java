package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.groupdto.ListGroupDTO;
import org.fms.training.dto.groupdto.ReadGroupDTO;
import org.fms.training.dto.groupdto.SaveGroupDTO;
import org.fms.training.entity.Group;
import org.fms.training.entity.User;
import org.fms.training.entity.UserGroup;
import org.fms.training.enums.GroupStatus;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.mapper.GroupMapper;
import org.fms.training.repository.GroupRepository;
import org.fms.training.repository.UserGroupRepository;
import org.fms.training.repository.UserRepository;
import org.fms.training.service.EmailService;
import org.fms.training.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

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

    @Override
    public Group existsByGroupName(String name) {
        return groupRepository.findByGroupName(name).orElse(null);
    }

    @Override
    public Group existsByGroupCode(String code) {
        return groupRepository.findByGroupCode(code).orElse(null);
    }
}