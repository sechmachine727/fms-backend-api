package org.fms.training.repository;

import org.fms.training.common.entity.Topic;
import org.fms.training.common.entity.TopicAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicAssessmentRepository extends JpaRepository<TopicAssessment, Integer> {

    @Query("SELECT ta FROM TopicAssessment ta WHERE ta.topic.id = :topicId")
    List<TopicAssessment> findTopicAssessmentsByTopicId(@Param("topicId") Integer topicId);

    List<TopicAssessment> findByTopic(Topic topic);

    void deleteByTopic(Topic topic);
}