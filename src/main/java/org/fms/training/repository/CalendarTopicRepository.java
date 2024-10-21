package org.fms.training.repository;

import org.fms.training.common.entity.CalendarTopic;
import org.fms.training.common.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarTopicRepository extends JpaRepository<CalendarTopic, Integer> {
    List<CalendarTopic> findAllByGroup(Group group);
}
