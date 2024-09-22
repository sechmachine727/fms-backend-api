package org.fms.training.repository;

import org.fms.training.entity.Trainer;
import org.springframework.data.repository.ListCrudRepository;

public interface TrainerRepository extends ListCrudRepository<Trainer, Integer> {
}
