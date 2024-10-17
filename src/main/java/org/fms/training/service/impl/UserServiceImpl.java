package org.fms.training.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.userdto.ChangePasswordDTO;
import org.fms.training.common.dto.userdto.ClassAdminDTO;
import org.fms.training.common.dto.userdto.ReadUserDTO;
import org.fms.training.common.dto.userdto.SaveUserDTO;
import org.fms.training.common.entity.Role;
import org.fms.training.common.entity.User;
import org.fms.training.common.entity.UserRole;
import org.fms.training.common.enums.Status;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.common.mapper.UserMapper;
import org.fms.training.repository.RoleRepository;
import org.fms.training.repository.TrainerRepository;
import org.fms.training.repository.UserRepository;
import org.fms.training.repository.UserRoleRepository;
import org.fms.training.service.EmailService;
import org.fms.training.service.UserService;
import org.fms.training.util.PasswordUtil;
import org.fms.training.util.Validation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final TrainerRepository trainerRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Transactional
    @Override
    public SaveUserDTO register(SaveUserDTO saveUserDTO) throws MessagingException {
        String plainPassword = PasswordUtil.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(plainPassword);
        User user = userMapper.toUserEntity(saveUserDTO);
        user.setEncryptedPassword(encodedPassword);
        User savedUser = userRepository.save(user);

        List<Role> assignedRoles = new ArrayList<>();
        if (saveUserDTO.getRoles() != null) {
            List<UserRole> userRoles = saveUserDTO.getRoles().stream()
                    .map(roleId -> {
                        Role role = roleRepository.findById(roleId)
                                .orElseThrow(() -> new RuntimeException("Role does not exist " + roleId));
                        assignedRoles.add(role);
                        UserRole userRole = new UserRole();
                        userRole.setUser(savedUser);
                        userRole.setRole(role);
                        return userRole;
                    })
                    .toList();
            userRoleRepository.saveAll(userRoles);
            savedUser.setUserRoles(userRoles);
        }

        // Prepare role names as a string
        String rolesString = assignedRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.joining(", "));

        // Prepare email variables
        Map<String, Object> emailVariables = Map.of(
                "account", savedUser.getAccount(),
                "password", plainPassword,
                "roles", rolesString
        );

        // Send email asynchronously
        emailService.sendHtmlEmail(savedUser.getEmail(), "Welcome to FMS", "welcome-email", emailVariables);

        return userMapper.toSaveUserDTO(savedUser);
    }


    @Override
    public User findByAccount(String account) {
        return userRepository.findByAccount(account).orElse(null);
    }

    @Override
    public User existsByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User existsByAccount(String account) {
        return userRepository.findByAccount(account).orElse(null);
    }

    @Override
    public User existsByEmployeeId(String employeeId) {
        return userRepository.findByEmployeeId(employeeId).orElse(null);
    }

    @Override
    public boolean isValidUser(SaveUserDTO saveUserDTO, Map<String, String> errors) {
        if (saveUserDTO == null || saveUserDTO.getAccount() == null || saveUserDTO.getEmail() == null) {
            return false;
        }

        boolean checkExistingAccount = existsByAccount(saveUserDTO.getAccount()) != null;
        boolean checkExistingEmail = existsByEmail(saveUserDTO.getEmail()) != null;
        boolean checkExistingEmployeeId = existsByEmployeeId(saveUserDTO.getEmployeeId()) != null;

        if (checkExistingAccount || checkExistingEmail || checkExistingEmployeeId) {
            if (checkExistingAccount) {
                errors.put("account", "User already exists in the system");
            }
            if (checkExistingEmail) {
                errors.put("email", "Email already exists in the system");
            }
            if (checkExistingEmployeeId) {
                errors.put("employeeId", "EmployeeId already exists in the system");
            }
            return false;
        }

        return !saveUserDTO.getAccount().isBlank() &&
                !saveUserDTO.getEmail().isBlank() &&
                Validation.isEmailValid(saveUserDTO.getEmail()) &&
                errors.isEmpty();
    }

    @Override
    public boolean isValidUserForUpdate(Integer userId, SaveUserDTO saveUserDTO, Map<String, String> errors) {
        if (saveUserDTO == null || saveUserDTO.getAccount() == null || saveUserDTO.getEmail() == null) {
            return false;
        }
        boolean checkExistingAccount = existsByAccount(saveUserDTO.getAccount()) != null && !Objects.equals(existsByAccount(saveUserDTO.getAccount()).getId(), userId);
        boolean checkExistingEmail = existsByEmail(saveUserDTO.getEmail()) != null && !Objects.equals(existsByEmail(saveUserDTO.getEmail()).getId(), userId);
        boolean checkExistingEmployeeId = existsByEmployeeId(saveUserDTO.getEmployeeId()) != null && !Objects.equals(existsByEmployeeId(saveUserDTO.getEmployeeId()).getId(), userId);

        if (checkExistingAccount || checkExistingEmail || checkExistingEmployeeId) {
            if (checkExistingAccount) {
                errors.put("account", "User already exists in the system");
            }
            if (checkExistingEmail) {
                errors.put("email", "Email already exists in the system");
            }
            if (checkExistingEmployeeId) {
                errors.put("employeeId", "EmployeeId already exists in the system");
            }
            return false;
        }

        return !saveUserDTO.getAccount().isBlank() &&
                !saveUserDTO.getEmail().isBlank() &&
                Validation.isEmailValid(saveUserDTO.getEmail()) &&
                errors.isEmpty();
    }

    @Override
    public boolean isValidUserForChangePassword(String account, ChangePasswordDTO data, Map<String, String> errors) {
        User user = userRepository.findByAccount(account).orElse(null);
        if(null == user) {
            errors.put("account", "User not found");
        }
        if(!passwordEncoder.matches(data.getOldPassword(), user.getEncryptedPassword())) {
            errors.put("oldPassword", "The old password you entered is incorrect");

        }
        if(data.getNewPassword().length() < 8) {
            errors.put("newPassword", "Password must be at least 8 characters long");
        }
        return errors.isEmpty();
    }


    @Override
    public Optional<List<ReadUserDTO>> findAll(String search) {
        List<User> users = userRepository.findByEmailContainingIgnoreCaseAndAccountContainingIgnoreCaseAndEmployeeIdContainingIgnoreCase(search);
        return Optional.of(users.stream()
                .map(userMapper::toReadUserDTO)
                .toList());
    }

    @Override
    public Optional<ReadUserDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toReadUserDTO);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getStatus() == null || user.getStatus() != Status.ACTIVE) {
            throw new UsernameNotFoundException("User is not active");
        }
        return Optional.of(user)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User detail not found for the user " + username));
    }

    @Transactional
    @Override
    public void updateUserInfo(Integer userId, SaveUserDTO saveUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserRole> existingRoles = userRoleRepository.findByUserId(userId);
        userRoleRepository.deleteAll(existingRoles);

        String plainPassword = null;
        if (!user.getAccount().equals(saveUserDTO.getAccount()) || !user.getEmail().equals(saveUserDTO.getEmail())) {
            plainPassword = PasswordUtil.generateRandomPassword();
            String encodedPassword = passwordEncoder.encode(plainPassword);
            user.setEncryptedPassword(encodedPassword);
        }

        userMapper.updateUserFromDTO(saveUserDTO, user);

        List<UserRole> newRoles = saveUserDTO.getRoles().stream()
                .map(roleId -> {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                    UserRole userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(role);
                    return userRole;
                })
                .toList();
        userRoleRepository.saveAll(newRoles);

        userRepository.save(user);

        String rolesString = newRoles.stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.joining(", "));

        if (plainPassword != null) {
            Map<String, Object> emailVariables = Map.of(
                    "account", user.getAccount(),
                    "password", plainPassword,
                    "roles", rolesString
            );

            emailService.sendHtmlEmail(user.getEmail(), "Your account has been updated", "welcome-email", emailVariables);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<ClassAdminDTO> getClassAdminUsers() {
        List<User> classAdminUsers = userRoleRepository.findUsersByRoleName("GROUP_ADMIN");
        return classAdminUsers.stream()
                .map(userMapper::toClassAdminDTO)
                .toList();
    }

    @Transactional
    @Override
    public Status toggleUserStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Status newStatus = user.getStatus() == Status.ACTIVE ? Status.INACTIVE : Status.ACTIVE;
        user.setStatus(newStatus);
        userRepository.save(user);
        return newStatus;
    }

    @Override
    public void changePassword(String account, ChangePasswordDTO data) {
        User user = userRepository.findByAccount(account).orElse(null);
        if(user != null) {
            String encodedPassword = passwordEncoder.encode(data.getNewPassword());
            user.setEncryptedPassword(encodedPassword);
            userRepository.save(user);
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getEncryptedPassword(), grantedAuthorities);
    }
}
