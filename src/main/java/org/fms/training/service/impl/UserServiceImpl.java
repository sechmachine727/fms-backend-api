package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.RoleDTO;
import org.fms.training.dto.UserDTO;
import org.fms.training.entity.Role;
import org.fms.training.entity.User;
import org.fms.training.entity.UserRole;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public User register(UserDTO userDTO) {
            User user = new User();
            user.setAccount(userDTO.getAccount());
            user.setEmail(userDTO.getEmail());
            user.setEmployeeId(userDTO.getEmployeeId());
            user.setContactType(userDTO.getContactType());
            user.setDepartment(userDTO.getDepartment());
            user.setStatus(user.isStatus());

            String encodedPassword = passwordEncoder.encode("1234");
            user.setEncryptedPassword(encodedPassword);

            User savedUser = userRepository.save(user);

        if (userDTO.getRoles() != null) {
            List<UserRole> userRoles = new ArrayList<>();
            for (Integer roleId : userDTO.getRoles()) {
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
        return savedUser;

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
    public boolean isValidUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getAccount() == null || userDTO.getEmail() == null) {
            return false;
        }
        return !userDTO.getAccount().isBlank() &&
                !userDTO.getEmail().isBlank() &&
                Validation.isEmailValid(userDTO.getEmail()) &&
                !userDTO.getDepartment().isBlank();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccount(username).orElse(null);
        return userRepository.findByAccount(username)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("Userdetail not found for the user " + username));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getEncryptedPassword(), grantedAuthorities);
    }
}
