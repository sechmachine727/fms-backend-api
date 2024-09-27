package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.userdto.RoleDTO;
import org.fms.training.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/roles")
@RestController()
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<List<RoleDTO>> getAll() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

}
