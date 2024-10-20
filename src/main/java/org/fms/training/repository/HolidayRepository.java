package org.fms.training.repository;

import org.fms.training.common.entity.Holiday;
import org.fms.training.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
    List<Holiday> findAllByStatus(Status status);
}
