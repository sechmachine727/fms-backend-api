package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.Role;
import org.fms.training.entity.User;
import org.fms.training.entity.UserRole;
import org.fms.training.enums.Status;
import org.fms.training.mapper.UserMapper;
import org.fms.training.repository.RoleRepository;
import org.fms.training.repository.UserRepository;
import org.fms.training.repository.UserRoleRepository;
import org.fms.training.service.UserService;
import org.fms.training.util.Validation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public SaveUserDTO register(SaveUserDTO saveUserDTO) {
        User user = userMapper.toUserEntity(saveUserDTO);
        String encodedPassword = passwordEncoder.encode("1234");
        user.setEncryptedPassword(encodedPassword);
        User savedUser = userRepository.save(user);

        if (saveUserDTO.getRoles() != null) {
            List<UserRole> userRoles = new ArrayList<>();
            for (Integer roleId : saveUserDTO.getRoles()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role does not exist " + roleId));
                UserRole userRole = new UserRole();
                userRole.setUser(savedUser);
                userRole.setRole(role);
                userRoles.add(userRole);
            }
            userRoleRepository.saveAll(userRoles);
            user.setUserRoles(userRoles);
        }
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
    public boolean isValidUser(SaveUserDTO saveUserDTO) {
        if (saveUserDTO == null || saveUserDTO.getAccount() == null || saveUserDTO.getEmail() == null) {
            return false;
        }
        return !saveUserDTO.getAccount().isBlank() &&
                !saveUserDTO.getEmail().isBlank() &&
                Validation.isEmailValid(saveUserDTO.getEmail()) &&
                !saveUserDTO.getDepartmentId().equals(0);
    }

    @Override
    public Optional<List<ReadUserDTO>> findAll() {
        List<User> users = userRepository.findAll();
        List<ReadUserDTO> listUserDTOs = users.stream()
                .map(userMapper::toReadUserDTO)
                .collect(Collectors.toList());
        return Optional.of(listUserDTOs);
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Remove existing roles
        List<UserRole> existingRoles = userRoleRepository.findByUserId(userId);
        userRoleRepository.deleteAll(existingRoles);

        //Update user
        userMapper.updateUserFromDTO(saveUserDTO, user);

        //Add new roles
        List<UserRole> newRoles = saveUserDTO.getRoles().stream()
                .map(roleId -> {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    UserRole userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(role);
                    return userRole;
                })
                .collect(Collectors.toList());
        userRoleRepository.saveAll(newRoles);
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getEncryptedPassword(), grantedAuthorities);
    }
}
