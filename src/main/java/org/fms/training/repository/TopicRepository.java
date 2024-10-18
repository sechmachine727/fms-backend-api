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
    List<Topic> getAllByOrderByLastModifiedDateDesc();

    @Query("SELECT t FROM Topic t WHERE t.id = :id")
    Optional<Topic> findTopicById(@Param("id") Integer id);

    List<Topic> findByStatus(Status status);

    Optional<Topic> findByTopicCodeAndVersion(String topicCode, String version);
}
