package org.fms.training.repository;

import org.fms.training.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findBySiteId(Integer siteId);
}
