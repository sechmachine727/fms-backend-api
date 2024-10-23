package org.fms.training.repository;

import org.fms.training.common.entity.Topic;
import org.fms.training.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    @Query(value = "SELECT * FROM Topic ORDER BY COALESCE(Last_Modified_Date, TIMESTAMP '1970-01-01') DESC", nativeQuery = true)
    List<Topic> getAllByOrderByLastModifiedDateDesc();

    @Query("SELECT t FROM Topic t WHERE t.id = :id")
    Optional<Topic> findTopicById(@Param("id") Integer id);

    List<Topic> findByStatus(Status status);

    Optional<Topic> findByTopicCodeAndVersion(String topicCode, String version);

    @Query("SELECT t FROM Topic t JOIN TopicTrainingProgram ttp ON t.id = ttp.topic.id " +
            "JOIN TrainingProgram tp ON ttp.trainingProgram.id = tp.id " +
            "JOIN Group g on g.trainingProgram.id = tp.id WHERE g.id = :groupId")
    List<Topic> findTopicsByGroupId(@Param("groupId") Integer groupId);
}
