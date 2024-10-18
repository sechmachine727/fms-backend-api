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
import org.fms.training.common.mapper.UserMapper;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.repository.*;
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
    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public SaveUserDTO register(SaveUserDTO saveUserDTO) throws MessagingException {
        validFieldsCheck(saveUserDTO);

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
                                .orElseThrow(() -> new ResourceNotFoundException("Role does not exist " + roleId));
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

    private void validFieldsCheck(SaveUserDTO saveUserDTO) {
        Map<String, String> errors = new HashMap<>();

        if (existsByAccount(saveUserDTO.getAccount()) != null) {
            errors.put("account", "User already exists in the system");
        }
        if (existsByEmail(saveUserDTO.getEmail()) != null) {
            errors.put("email", "Email already exists in the system");
        }
        if (existsByEmployeeId(saveUserDTO.getEmployeeId()) != null) {
            errors.put("employeeId", "EmployeeId already exists in the system");
        }

        if (saveUserDTO.getAccount().isBlank() || saveUserDTO.getEmail().isBlank() || !Validation.isEmailValid(saveUserDTO.getEmail())) {
            errors.put("validation", "Invalid account or email");
        }

        validateRoleIds(saveUserDTO.getRoles(), errors);

        validateDepartmentId(saveUserDTO.getDepartmentId(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validFieldsCheckForUpdate(Integer userId, SaveUserDTO saveUserDTO) {
        Map<String, String> errors = new HashMap<>();

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!existingUser.getAccount().equals(saveUserDTO.getAccount()) && existsByAccount(saveUserDTO.getAccount()) != null) {
            errors.put("account", "Account already exists");
        }

        if (!existingUser.getEmail().equals(saveUserDTO.getEmail()) && existsByEmail(saveUserDTO.getEmail()) != null) {
            errors.put("email", "Email already exists");
        }

        if (!existingUser.getEmployeeId().equals(saveUserDTO.getEmployeeId()) && existsByEmployeeId(saveUserDTO.getEmployeeId()) != null) {
            errors.put("employeeId", "Employee ID already exists");
        }

        if (saveUserDTO.getAccount().isBlank() || saveUserDTO.getEmail().isBlank() || !Validation.isEmailValid(saveUserDTO.getEmail())) {
            errors.put("validation", "Invalid account or email");
        }

        validateRoleIds(saveUserDTO.getRoles(), errors);
        validateDepartmentId(saveUserDTO.getDepartmentId(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }


    private void validateRoleIds(List<Integer> roleIds, Map<String, String> errors) {
        List<Integer> invalidRoleIds = roleIds.stream()
                .filter(roleId -> !roleRepository.existsById(roleId))
                .toList();

        if (!invalidRoleIds.isEmpty()) {
            errors.put("roleIds", "Invalid role IDs: " + invalidRoleIds);
        }
    }

    private void validateDepartmentId(Integer departmentId, Map<String, String> errors) {
        if (!departmentRepository.existsById(departmentId)) {
            errors.put("departmentId", "Invalid department ID: " + departmentId);
        }
    }

    @Override
    public Optional<List<ReadUserDTO>> findAll() {
        List<User> users = userRepository.getAllByOrderByAccountAsc();
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
        validFieldsCheckForUpdate(userId, saveUserDTO);

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
        isValidUserForChangePassword(account, data);
        User user = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String encodedPassword = passwordEncoder.encode(data.getNewPassword());
        user.setEncryptedPassword(encodedPassword);
        userRepository.save(user);
    }

    private void isValidUserForChangePassword(String account, ChangePasswordDTO data) {
        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByAccount(account).orElse(null);

        if (user == null) {
            errors.put("account", "User not found");
        } else {
            if (!passwordEncoder.matches(data.getOldPassword(), user.getEncryptedPassword())) {
                errors.put("oldPassword", "The old password you entered is incorrect");
            }
        }

        if (data.getNewPassword().length() < 8) {
            errors.put("newPassword", "Password must be at least 8 characters long");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getEncryptedPassword(), grantedAuthorities);
    }
}