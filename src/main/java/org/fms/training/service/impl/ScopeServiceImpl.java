package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.entity.Scope;
import org.fms.training.repository.ScopeRepository;
import org.fms.training.service.ScopeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository scopeRepository;

    @Override
    public Optional<List<Scope>> getAllScopes() {
        List<Scope> scopes = scopeRepository.findAll();
        return scopes.isEmpty() ? Optional.empty() : Optional.of(scopes);
    }
}
