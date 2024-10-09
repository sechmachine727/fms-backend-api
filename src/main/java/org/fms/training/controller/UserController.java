package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.userdto.ClassAdminDTO;
import org.fms.training.dto.userdto.ReadUserDTO;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.enums.Status;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.repository.UserRepository;
import org.fms.training.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String, String>> updateUserInfo(@PathVariable Integer id, @RequestBody SaveUserDTO saveUserDTO) {
        Map<String, String> errors = new HashMap<>();
        try {
            if (!userService.isValidUserForUpdate(id, saveUserDTO, errors))
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            userService.updateUserInfo(id, saveUserDTO);
            Map<String, String> responseSuccess = new HashMap<>();
            responseSuccess.put("success", "Update user info success");
            return ResponseEntity.ok(responseSuccess);
        } catch (Exception e) {
            errors.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errors);
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Map<String, String>> updateUserStatus(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            Status newStatus = userService.toggleUserStatus(id);
            response.put("success", "User status updated successfully to " + newStatus);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Update user status failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> register(@RequestBody SaveUserDTO saveUserDTO) {
        Map<String, String> errors = new HashMap<>();
        try {
            if (!userService.isValidUser(saveUserDTO, errors))
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            SaveUserDTO result = userService.register(saveUserDTO);
            Map<String, String> responseSuccess = new HashMap<>();
            responseSuccess.put("success", "Save user success: " + result.getAccount());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseSuccess);
        } catch (Exception e) {
            errors.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errors);
        }
    }


    @GetMapping("/class-admins")
    public List<ClassAdminDTO> getClassAdmins() {
        return userService.getClassAdminUsers();
    }
}
