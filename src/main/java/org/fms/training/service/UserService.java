package org.fms.training.service;

import org.fms.training.dto.userdto.ClassAdminDTO;
import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    SaveUserDTO register(SaveUserDTO saveUserDTO);

    User findByAccount(String account);

    User existsByEmail(String email);

    User existsByAccount(String account);

    User existsByEmployeeId(String employeeId);

    boolean isValidUser(SaveUserDTO saveUserDTO);

    Optional<List<ReadUserDTO>> findAll(String search);

    Optional<ReadUserDTO> findById(Integer id);

    void updateUserInfo(Integer userId, SaveUserDTO saveUserDTO);
    List<ClassAdminDTO> getClassAdminUsers();
}
