package org.fms.training.service;

import jakarta.mail.MessagingException;
import org.fms.training.dto.userdto.ClassAdminDTO;
import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.User;
import org.fms.training.enums.Status;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    SaveUserDTO register(SaveUserDTO saveUserDTO) throws MessagingException;

    User findByAccount(String account);

    User existsByEmail(String email);

    User existsByAccount(String account);

    User existsByEmployeeId(String employeeId);

    boolean isValidUser(SaveUserDTO saveUserDTO, Map<String, String> errors);

    boolean isValidUserForUpdate(Integer userId, SaveUserDTO saveUserDTO, Map<String, String> errors);

    Optional<List<ReadUserDTO>> findAll(String search);

    Optional<ReadUserDTO> findById(Integer id);

    void updateUserInfo(Integer userId, SaveUserDTO saveUserDTO);

    List<ClassAdminDTO> getClassAdminUsers();

    @Transactional
    Status toggleUserStatus(Integer userId);
}
