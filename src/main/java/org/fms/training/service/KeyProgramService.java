package org.fms.training.service;

import org.fms.training.entity.KeyProgram;

import java.util.List;
import java.util.Optional;

public interface KeyProgramService {
    Optional<List<KeyProgram>> getAllKeyPrograms();
}
