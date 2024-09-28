package org.fms.training.repository;

import org.fms.training.entity.TechnicalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicalGroupRepository extends JpaRepository<TechnicalGroup, Integer> {
    Optional<TechnicalGroup> findByCode(String code);
}
