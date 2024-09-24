package org.fms.training.service;

import org.fms.training.dto.UserDTO;
import org.fms.training.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(UserDTO userDTO);
    User findByAccount(String account);
    User existsByEmail(String email);
    User existsByAccount(String account);
    boolean isValidUser(UserDTO userDTO);
}
