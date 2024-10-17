package org.fms.training.repository;

import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicTrainingProgramRepository extends JpaRepository<TopicTrainingProgram, Integer> {
    List<TopicTrainingProgram> findByTrainingProgramId(Integer trainingProgramId);

    boolean existsByTopic(Topic topic);
}