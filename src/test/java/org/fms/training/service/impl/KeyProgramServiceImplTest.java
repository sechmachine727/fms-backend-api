package org.fms.training.service.impl;

import org.fms.training.common.entity.KeyProgram;
import org.fms.training.repository.KeyProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class KeyProgramServiceImplTest {

    @Mock
    private KeyProgramRepository keyProgramRepository;

    @InjectMocks
    private KeyProgramServiceImpl keyProgramService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllKeyPrograms_shouldReturnListOfKeyPrograms_whenKeyProgramsExist() {
        // given
        KeyProgram keyProgram1 = new KeyProgram();
        KeyProgram keyProgram2 = new KeyProgram();
        given(keyProgramRepository.findAll()).willReturn(List.of(keyProgram1, keyProgram2));

        // when
        Optional<List<KeyProgram>> result = keyProgramService.getAllKeyPrograms();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        verify(keyProgramRepository, times(1)).findAll();
    }

    @Test
    void getAllKeyPrograms_shouldReturnEmptyOptional_whenNoKeyProgramsExist() {
        // given
        given(keyProgramRepository.findAll()).willReturn(List.of());

        // when
        Optional<List<KeyProgram>> result = keyProgramService.getAllKeyPrograms();

        // then
        assertThat(result).isEmpty();
        verify(keyProgramRepository, times(1)).findAll();
    }
}
