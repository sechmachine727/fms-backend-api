package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.userdto.ClassAdminDTO;
import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.entity.User;
import org.fms.training.enums.Status;
import org.fms.training.repository.UserRepository;
import org.fms.training.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ReadUserDTO>> findAll(
            @RequestParam(required = false) String search
    ) {
        Optional<List<ReadUserDTO>> result = userService.findAll(search);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadUserDTO> findById(@PathVariable Integer id) {
        Optional<ReadUserDTO> result = userService.findById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserInfo(@PathVariable Integer id, @RequestBody SaveUserDTO saveUserDTO) {
        try {
            userService.updateUserInfo(id, saveUserDTO);
            return ResponseEntity.ok("Update user info success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<String> updateUserStatus(@PathVariable Integer id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body("User id is required");
            }
            Optional<ReadUserDTO> userOptional = userService.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            if (user.getStatus().equals(Status.ACTIVE)) {
                userService.updateUserStatus(id, Status.INACTIVE);
                return ResponseEntity.ok("Update user status to: " + Status.INACTIVE);
            } else {
                userService.updateUserStatus(id, Status.ACTIVE);
                return ResponseEntity.ok("Update user status to: " + Status.ACTIVE);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update user status failed");
        }
    }

    @PostMapping()
    public ResponseEntity<String> register(@RequestBody SaveUserDTO saveUserDTO) {
        try {
            if (!userService.isValidUser(saveUserDTO)) {
                return new ResponseEntity<>("Input invalid", HttpStatus.BAD_REQUEST);
            }
            if (userService.existsByAccount(saveUserDTO.getAccount()) != null) {
                return new ResponseEntity<>("User already exists in the system", HttpStatus.BAD_REQUEST);
            }
            if (userService.existsByEmail(saveUserDTO.getEmail()) != null) {
                return new ResponseEntity<>("Email already exists in the system", HttpStatus.BAD_REQUEST);
            }
            if (userService.existsByEmployeeId(saveUserDTO.getEmployeeId()) != null) {
                return new ResponseEntity<>("EmployeeId already exists in the system", HttpStatus.BAD_REQUEST);
            }
            SaveUserDTO result = userService.register(saveUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Save user success: " + result.getAccount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Save user failed");
        }
    }

    @GetMapping("/class-admins")
    public List<ClassAdminDTO> getClassAdmins() {
        return userService.getClassAdminUsers();
    }
}
