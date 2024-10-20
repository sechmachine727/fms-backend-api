package org.fms.training.repository;

import org.fms.training.common.entity.SlotTimeSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotTimeSuggestionRepository extends JpaRepository<SlotTimeSuggestion, Integer> {
}
