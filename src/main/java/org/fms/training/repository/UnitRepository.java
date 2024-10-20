package org.fms.training.repository;

import org.fms.training.common.entity.Topic;
import org.fms.training.common.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

    @Query("SELECT u FROM Unit u WHERE u.topic.id = :topicId")
    List<Unit> findUnitsByTopicId(@Param("topicId") Integer topicId);

    List<Unit> findByTopic(Topic topic);
}
