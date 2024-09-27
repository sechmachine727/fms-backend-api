package org.fms.training.service;

import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    SaveUserDTO register(SaveUserDTO saveUserDTO);

    User findByAccount(String account);

    User existsByEmail(String email);

    User existsByAccount(String account);

    boolean isValidUser(SaveUserDTO saveUserDTO);
}
