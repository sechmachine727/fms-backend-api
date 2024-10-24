package org.fms.training.repository;

import org.fms.training.common.entity.FormatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatTypeRepository extends JpaRepository<FormatType, Integer> {
}
