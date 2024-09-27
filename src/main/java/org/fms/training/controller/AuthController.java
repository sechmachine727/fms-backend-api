package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.config.TokenProvider;
import org.fms.training.dto.AuthenticationResponse;
import org.fms.training.dto.userdto.SaveUserDTO;
import org.fms.training.dto.userdto.UserLoginDTO;
import org.fms.training.entity.User;
import org.fms.training.enums.Status;
import org.fms.training.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController()
public class AuthController {
    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/register")
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
            SaveUserDTO result = userService.register(saveUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Save user success: " + result.getAccount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            User user = userService.findByAccount(userLoginDTO.getAccount());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("Username or password is incorrect"));
            }

            if (user.getStatus() != Status.ACTIVE) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("User is inactive"));
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userLoginDTO.getAccount(),
                    userLoginDTO.getPassword()
            );
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, "Bearer ");
            return ResponseEntity.ok(authenticationResponse);
        } catch (AuthenticationException e) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse("Username or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }
    }
}
