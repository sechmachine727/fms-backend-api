package org.fms.training.repository;

import org.fms.training.common.entity.Topic;
import org.fms.training.common.entity.TopicAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicAssessmentRepository extends JpaRepository<TopicAssessment, Integer> {

    @Query("SELECT ta FROM TopicAssessment ta WHERE ta.topic.id = :topicId")
    List<TopicAssessment> findTopicAssessmentsByTopicId(@Param("topicId") Integer topicId);

    void deleteByTopic(Topic topic);
}