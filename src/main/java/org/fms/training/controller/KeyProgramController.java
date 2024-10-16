package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.entity.KeyProgram;
import org.fms.training.service.KeyProgramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/key-programs")
@RequiredArgsConstructor
public class KeyProgramController {

    private final KeyProgramService keyProgramService;

    @GetMapping
    public ResponseEntity<List<KeyProgram>> getAllKeyPrograms() {
        Optional<List<KeyProgram>> keyProgramsOpt = keyProgramService.getAllKeyPrograms();
        return keyProgramsOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
