package org.fms.training.repository;

import org.fms.training.entity.TechnicalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalGroupRepository extends JpaRepository<TechnicalGroup, Integer> {
}
