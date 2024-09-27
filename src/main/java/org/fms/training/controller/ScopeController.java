package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.Scope;
import org.fms.training.service.ScopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/scopes")
@RequiredArgsConstructor
public class ScopeController {

    private final ScopeService scopeService;

    @GetMapping
    public ResponseEntity<List<Scope>> getAllScopes() {
        Optional<List<Scope>> scopesOpt = scopeService.getAllScopes();
        return scopesOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
