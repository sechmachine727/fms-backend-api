package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicTrainingProgram;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.TopicTrainingProgramRepository;
import org.fms.training.repository.TrainingProgramRepository;
import org.fms.training.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final TopicRepository topicRepository;
    private final TopicTrainingProgramRepository topicTrainingProgramRepository;

    @Override
    public Optional<List<TrainingProgram>> findAll() {
        return Optional.of(trainingProgramRepository.findAll());
    }

    @Override
    public Optional<TrainingProgram> findById(Integer id) {
        return trainingProgramRepository.findById(id);
    }

    @Transactional
    @Override
    public TrainingProgram createTrainingProgram(TrainingProgram trainingProgram, List<Integer> topicIds) {
        TrainingProgram savedTrainingProgram = trainingProgramRepository.save(trainingProgram);
        for (Integer topicId : topicIds) {
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));
            TopicTrainingProgram topicTrainingProgram = new TopicTrainingProgram();
            topicTrainingProgram.setTrainingProgram(savedTrainingProgram);
            topicTrainingProgram.setTopic(topic);
            topicTrainingProgramRepository.save(topicTrainingProgram);
        }
        return savedTrainingProgram;
    }

    @Transactional
    @Override
    public TrainingProgram updateTrainingProgram(Integer trainingProgramId, TrainingProgram trainingProgram, List<Topic> topics) {
        TrainingProgram existingTrainingProgram = trainingProgramRepository.findById(trainingProgramId).orElseThrow(() -> new RuntimeException("TrainingProgram not found"));
        existingTrainingProgram.setName(trainingProgram.getName());
        existingTrainingProgram.setDescription(trainingProgram.getDescription());
        trainingProgramRepository.save(existingTrainingProgram);

        // Remove existing topics
        List<TopicTrainingProgram> existingTopics = topicTrainingProgramRepository.findByTrainingProgramId(trainingProgramId);
        topicTrainingProgramRepository.deleteAll(existingTopics);

        // Add new topics
        for (Topic topic : topics) {
            topicRepository.save(topic);
            TopicTrainingProgram topicTrainingProgram = new TopicTrainingProgram();
            topicTrainingProgram.setTrainingProgram(existingTrainingProgram);
            topicTrainingProgram.setTopic(topic);
            topicTrainingProgramRepository.save(topicTrainingProgram);
        }
        return existingTrainingProgram;
    }
}
