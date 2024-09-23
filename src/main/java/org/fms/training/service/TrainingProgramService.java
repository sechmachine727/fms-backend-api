package org.fms.training.service;

import org.fms.training.entity.Topic;
import org.fms.training.entity.TrainingProgram;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainingProgramService {
    Optional<List<TrainingProgram>> findAll();

    Optional<TrainingProgram> findById(Integer id);

    @Transactional
    TrainingProgram createTrainingProgram(TrainingProgram trainingProgram, List<Integer> topicIds);

    @Transactional
    TrainingProgram updateTrainingProgram(Integer trainingProgramId, TrainingProgram trainingProgram, List<Topic> topics);
}
