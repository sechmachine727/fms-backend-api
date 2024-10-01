package org.fms.training.repository;

import org.fms.training.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Page<Topic> findAll(Pageable pageable);

    Page<Topic> findByTopicCodeContainingOrTopicNameContaining(String topicCode, String topicName, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE t.id = :id")
    Optional<Topic> findTopicById(@Param("id") Integer id);
}
