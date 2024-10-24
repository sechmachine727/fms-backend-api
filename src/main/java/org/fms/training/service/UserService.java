package org.fms.training.service;

import jakarta.mail.MessagingException;
import org.fms.training.common.dto.userdto.ChangePasswordDTO;
import org.fms.training.common.dto.userdto.ClassAdminDTO;
import org.fms.training.common.dto.userdto.ReadUserDTO;
import org.fms.training.common.dto.userdto.SaveUserDTO;
import org.fms.training.common.entity.User;
import org.fms.training.common.enums.Status;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    SaveUserDTO register(SaveUserDTO saveUserDTO) throws MessagingException;

    User findByAccount(String account);

    User existsByEmail(String email);

    User existsByAccount(String account);

    User existsByEmployeeId(String employeeId);

    Optional<List<ReadUserDTO>> findAll();

    Optional<ReadUserDTO> findById(Integer id);

    void updateUserInfo(Integer userId, SaveUserDTO saveUserDTO);

    List<ClassAdminDTO> getClassAdminUsers();

    @Transactional
    Status toggleUserStatus(Integer userId);

    void changePassword(String account, ChangePasswordDTO data);

}
