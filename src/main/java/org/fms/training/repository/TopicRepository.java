package org.fms.training.repository;

import org.fms.training.entity.Topic;
import org.springframework.data.repository.ListCrudRepository;

public interface TopicRepository extends ListCrudRepository<Topic, Integer> {
}
