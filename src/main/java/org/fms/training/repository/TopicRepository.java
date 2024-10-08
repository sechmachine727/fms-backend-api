package org.fms.training.repository;

import org.fms.training.entity.Topic;
import org.fms.training.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    @Query("SELECT t FROM Topic t WHERE " +
            "(:search IS NULL OR :search = '' OR LOWER(t.topicCode) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR :search = '' OR LOWER(t.topicName) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Topic> findByTopicCodeContainingOrTopicNameContaining(
            @Param("search") String search);

    @Query("SELECT t FROM Topic t WHERE t.id = :id")
    Optional<Topic> findTopicById(@Param("id") Integer id);

    List<Topic> findByStatus(Status status);

    Optional<Topic> findByTopicCodeAndVersion(String topicCode, String version);
}
