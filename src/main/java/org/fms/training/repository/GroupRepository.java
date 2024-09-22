package org.fms.training.repository;

import org.fms.training.entity.Group;
import org.springframework.data.repository.ListCrudRepository;

public interface GroupRepository extends ListCrudRepository<Group, Integer> {
}
