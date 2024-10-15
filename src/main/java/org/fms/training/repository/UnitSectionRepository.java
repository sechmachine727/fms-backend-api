package org.fms.training.repository;

import org.fms.training.entity.Unit;
import org.fms.training.entity.UnitSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitSectionRepository extends JpaRepository<UnitSection, Integer> {
}
