package org.fms.training.repository;

import org.fms.training.common.entity.EntryInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryInformationRepository extends JpaRepository<EntryInformation, Integer> {
}
