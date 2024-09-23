package org.fms.training.repository;

import org.fms.training.entity.TopicTrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicTrainingProgramRepository extends JpaRepository<TopicTrainingProgram, Integer> {
    List<TopicTrainingProgram> findByTrainingProgramId(Integer trainingProgramId);
}