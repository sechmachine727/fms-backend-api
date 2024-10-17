package org.fms.training.service;

import org.fms.training.common.entity.Scope;

import java.util.List;
import java.util.Optional;

public interface ScopeService {
    Optional<List<Scope>> getAllScopes();
}
