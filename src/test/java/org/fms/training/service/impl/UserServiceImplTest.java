package org.fms.training.service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() throws Exception {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            // Initialization code if needed
        }
    }

    @Test
    void register_Success() {
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        User user = new User();
        given(userMapper.toUserEntity(saveUserDTO)).willReturn(user);
        given(passwordEncoder.encode("1234")).willReturn("encodedPassword");
        given(userRepository.save(user)).willReturn(user);
        given(userMapper.toSaveUserDTO(user)).willReturn(saveUserDTO);

        SaveUserDTO result = userService.register(saveUserDTO);

        assertThat(result).isEqualTo(saveUserDTO);
        then(userMapper).should().toUserEntity(saveUserDTO);
        then(passwordEncoder).should().encode("1234");
        then(userRepository).should().save(user);
        then(userMapper).should().toSaveUserDTO(user);
    }

    @Test
    void register_RoleNotFound() {
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setRoles(List.of(1));
        User user = new User();
        given(userMapper.toUserEntity(saveUserDTO)).willReturn(user);
        given(passwordEncoder.encode("1234")).willReturn("encodedPassword");
        given(userRepository.save(user)).willReturn(user);
        given(roleRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.register(saveUserDTO));
        then(userMapper).should().toUserEntity(saveUserDTO);
        then(passwordEncoder).should().encode("1234");
        then(userRepository).should().save(user);
        then(roleRepository).should().findById(1);
    }

    @Test
    void findByAccount_Success() {
        String account = "testAccount";
        User user = new User();
        given(userRepository.findByAccount(account)).willReturn(Optional.of(user));

        User result = userService.findByAccount(account);

        assertThat(result).isEqualTo(user);
        then(userRepository).should().findByAccount(account);
    }

    @Test
    void findByAccount_NotFound() {
        String account = "testAccount";
        given(userRepository.findByAccount(account)).willReturn(Optional.empty());

        User result = userService.findByAccount(account);

        assertThat(result).isNull();
        then(userRepository).should().findByAccount(account);
    }

    @Test
    void existsByEmail_Success() {
        String email = "test@example.com";
        User user = new User();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        User result = userService.existsByEmail(email);

        assertThat(result).isEqualTo(user);
        then(userRepository).should().findByEmail(email);
    }

    @Test
    void existsByEmail_NotFound() {
        String email = "test@example.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        User result = userService.existsByEmail(email);

        assertThat(result).isNull();
        then(userRepository).should().findByEmail(email);
    }

    @Test
    void existsByAccount_Success() {
        String account = "testAccount";
        User user = new User();
        given(userRepository.findByAccount(account)).willReturn(Optional.of(user));

        User result = userService.existsByAccount(account);

        assertThat(result).isEqualTo(user);
        then(userRepository).should().findByAccount(account);
    }

    @Test
    void existsByAccount_NotFound() {
        String account = "testAccount";
        given(userRepository.findByAccount(account)).willReturn(Optional.empty());

        User result = userService.existsByAccount(account);

        assertThat(result).isNull();
        then(userRepository).should().findByAccount(account);
    }

    @Test
    void existsByEmployeeId_Success() {
        String employeeId = "1234";
        User user = new User();
        given(userRepository.findByEmployeeId(employeeId)).willReturn(Optional.of(user));

        User result = userService.existsByEmployeeId(employeeId);

        assertThat(result).isEqualTo(user);
        then(userRepository).should().findByEmployeeId(employeeId);
    }

    @Test
    void existsByEmployeeId_NotFound() {
        String employeeId = "1234";
        given(userRepository.findByEmployeeId(employeeId)).willReturn(Optional.empty());

        User result = userService.existsByEmployeeId(employeeId);

        assertThat(result).isNull();
        then(userRepository).should().findByEmployeeId(employeeId);
    }

//    @Test
//    void isValidUser_Valid() {
//        SaveUserDTO saveUserDTO = new SaveUserDTO();
//        saveUserDTO.setAccount("testAccount");
//        saveUserDTO.setEmail("email@domain.com");
//        saveUserDTO.setDepartmentId(1);
//
//        boolean result = userService.isValidUser(saveUserDTO);
//
//        assertTrue(result);
//    }
//
//    @Test
//    void isValidUser_Invalid() {
//        SaveUserDTO saveUserDTO = new SaveUserDTO();
//        saveUserDTO.setAccount(null);
//        saveUserDTO.setEmail(null);
//        saveUserDTO.setDepartmentId(0);
//
//        boolean result = userService.isValidUser(saveUserDTO);
//
//        assertFalse(result);
//    }

    @Test
    void findAll() {
        User user1 = new User();
        User user2 = new User();
        ReadUserDTO readUserDTO1 = new ReadUserDTO();
        ReadUserDTO readUserDTO2 = new ReadUserDTO();
        List<User> users = List.of(user1, user2);

        given(userRepository.findByEmailContainingIgnoreCaseAndAccountContainingIgnoreCaseAndEmployeeIdContainingIgnoreCase("search"))
                .willReturn(users);
        given(userMapper.toReadUserDTO(user1)).willReturn(readUserDTO1);
        given(userMapper.toReadUserDTO(user2)).willReturn(readUserDTO2);

        Optional<List<ReadUserDTO>> result = userService.findAll("search");

        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        then(userRepository).should().findByEmailContainingIgnoreCaseAndAccountContainingIgnoreCaseAndEmployeeIdContainingIgnoreCase("search");
        then(userMapper).should(times(2)).toReadUserDTO(any(User.class));
    }

    @Test
    void findById() {
        Integer id = 1;
        User user = new User();
        ReadUserDTO readUserDTO = new ReadUserDTO();

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userMapper.toReadUserDTO(user)).willReturn(readUserDTO);

        Optional<ReadUserDTO> result = userService.findById(id);

        assertThat(result).isPresent().contains(readUserDTO);
        then(userRepository).should().findById(id);
        then(userMapper).should().toReadUserDTO(user);
    }

    @Test
    void loadUserByUsername_Success() {
        String username = "testAccount";
        User user = new User();
        user.setStatus(Status.ACTIVE);
        Role role = new Role();
        role.setId(1);
        role.setRoleName("ROLE_USER");
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        user.setUserRoles(List.of(userRole));

        given(userRepository.findByAccount(username)).willReturn(Optional.of(user));

        userService.loadUserByUsername(username);

        then(userRepository).should().findByAccount(username);
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String username = "testAccount";
        given(userRepository.findByAccount(username)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.loadUserByUsername(username));

        then(userRepository).should().findByAccount(username);
    }

    @Test
    void loadUserByUsername_UserNotActive() {
        String username = "testAccount";
        User user = new User();
        user.setStatus(Status.INACTIVE);
        given(userRepository.findByAccount(username)).willReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.loadUserByUsername(username));

        then(userRepository).should().findByAccount(username);
    }

    @Test
    void updateUserInfo_Success() {
        Integer userId = 1;
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setRoles(List.of(1));
        User user = new User();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(roleRepository.findById(anyInt())).willReturn(Optional.of(new Role()));

        userService.updateUserInfo(userId, saveUserDTO);

        then(userRepository).should().findById(userId);
        then(userMapper).should().updateUserFromDTO(saveUserDTO, user);
        then(userRoleRepository).should().deleteAll(anyList());
        then(userRoleRepository).should().saveAll(anyList());
    }

    @Test
    void updateUserInfo_UserNotFound() {
        Integer userId = 1;
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUserInfo(userId, saveUserDTO));
        then(userRepository).should().findById(userId);
        then(userMapper).shouldHaveNoInteractions();
        then(userRoleRepository).shouldHaveNoInteractions();
    }

    @Test
    void getClassAdminUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = List.of(user1, user2);

        given(userRoleRepository.findUsersByRoleName("ROLE")).willReturn(users);

        userService.getClassAdminUsers();

        then(userRoleRepository).should().findUsersByRoleName("ROLE");
        then(userMapper).should(times(2)).toClassAdminDTO(any(User.class));
    }

    @Test
    void updateUserStatus_UserNotFound() {
        Integer userId = 1;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUserStatus(userId, Status.ACTIVE));
        then(userRepository).should().findById(userId);
    }

    @Test
    void updateUserStatus_Success() {
        Integer userId = 1;
        User user = new User();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.updateUserStatus(userId, Status.ACTIVE);

        then(userRepository).should().findById(userId);
        then(userRepository).should().save(user);
    }
}