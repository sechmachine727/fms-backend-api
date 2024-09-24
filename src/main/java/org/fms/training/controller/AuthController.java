package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.config.TokenProvider;
import org.fms.training.dto.AuthenticationResponse;
import org.fms.training.dto.UserDTO;
import org.fms.training.dto.UserLoginDTO;
import org.fms.training.entity.User;
import org.fms.training.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController()
public class AuthController {
    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            if (!userService.isValidUser(userDTO)) {
                return new ResponseEntity<>("Input invalid", HttpStatus.BAD_REQUEST);
            }
            var result = userService.existsByAccount(userDTO.getAccount());
            var check = result != null;
            if (userService.existsByAccount(userDTO.getAccount()) != null) {
                return new ResponseEntity<>("User already exists in the system", HttpStatus.BAD_REQUEST);
            }

            if (userService.existsByEmail(userDTO.getEmail()) != null) {
                return new ResponseEntity<>("Email already exists in the system", HttpStatus.BAD_REQUEST);
            }
            User savedUser =  userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Save user success: ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userLoginDTO.getUsername(),
                    userLoginDTO.getPassword()
            );

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, "Bearer ");
            return ResponseEntity.ok(authenticationResponse);
        }catch (AuthenticationException e) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse("Account or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }
    }

}
