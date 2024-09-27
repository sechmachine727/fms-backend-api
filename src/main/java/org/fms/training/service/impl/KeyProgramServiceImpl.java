package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.KeyProgram;
import org.fms.training.repository.KeyProgramRepository;
import org.fms.training.service.KeyProgramService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeyProgramServiceImpl implements KeyProgramService {

    private final KeyProgramRepository keyProgramRepository;

    @Override
    public Optional<List<KeyProgram>> getAllKeyPrograms() {
        List<KeyProgram> keyPrograms = keyProgramRepository.findAll();
        return keyPrograms.isEmpty() ? Optional.empty() : Optional.of(keyPrograms);
    }
}
