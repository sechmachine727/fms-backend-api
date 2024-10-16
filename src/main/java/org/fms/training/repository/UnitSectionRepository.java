package org.fms.training.repository;

import org.fms.training.entity.Unit;
import org.fms.training.entity.UnitSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnitSectionRepository extends JpaRepository<UnitSection, Integer> {
    @Modifying
    @Query("DELETE FROM UnitSection us WHERE us.unit = :unit")
    void deleteByUnit(@Param("unit") Unit unit);
}
