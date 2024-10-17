package org.fms.training.service.impl;

import org.fms.training.common.entity.Scope;
import org.fms.training.repository.ScopeRepository;
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

class ScopeServiceImplTest {

    @Mock
    private ScopeRepository scopeRepository;

    @InjectMocks
    private ScopeServiceImpl scopeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllScopes_shouldReturnScopes_whenScopesExist() {
        // given
        Scope scope1 = new Scope();
        Scope scope2 = new Scope();
        List<Scope> scopes = List.of(scope1, scope2);
        given(scopeRepository.findAll()).willReturn(scopes);

        // when
        Optional<List<Scope>> result = scopeService.getAllScopes();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        verify(scopeRepository, times(1)).findAll();
    }

    @Test
    void getAllScopes_shouldReturnEmptyOptional_whenNoScopesExist() {
        // given
        given(scopeRepository.findAll()).willReturn(List.of());

        // when
        Optional<List<Scope>> result = scopeService.getAllScopes();

        // then
        assertThat(result).isEmpty();
        verify(scopeRepository, times(1)).findAll();
    }
}
