package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TechnicalGroup;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicTrainingProgram;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.enums.Status;
import org.fms.training.mapper.TrainingProgramMapper;
import org.fms.training.repository.TechnicalGroupRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.TopicTrainingProgramRepository;
import org.fms.training.repository.TrainingProgramRepository;
import org.fms.training.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final TopicRepository topicRepository;
    private final TopicTrainingProgramRepository topicTrainingProgramRepository;
    private final TechnicalGroupRepository technicalGroupRepository;
    private final TrainingProgramMapper trainingProgramMapper;

    @Override
    public Optional<List<ListTrainingProgramDTO>> findAll() {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findAll();
        List<ListTrainingProgramDTO> listTrainingProgramDTOs = trainingPrograms.stream()
                .map(trainingProgramMapper::toListTrainingProgramDTO)
                .collect(Collectors.toList());
        return Optional.of(listTrainingProgramDTOs);
    }

    @Override
    public Optional<ReadTrainingProgramDTO> findById(Integer id) {
        return trainingProgramRepository.findById(id)
                .map(trainingProgramMapper::toReadTrainingProgramDTO);
    }

    @Transactional
    @Override
    public void createTrainingProgram(SaveTrainingProgramDTO saveTrainingProgramDTO) {
        TrainingProgram trainingProgram = trainingProgramMapper.toTrainingProgramEntity(saveTrainingProgramDTO);
        TechnicalGroup technicalGroup = technicalGroupRepository.findById(saveTrainingProgramDTO.getTechnicalGroupId())
                .orElseThrow(() -> new RuntimeException("TechnicalGroup not found"));
        trainingProgram.setTechnicalGroup(technicalGroup);

        // Save the training program first to get its ID
        TrainingProgram savedTrainingProgram = trainingProgramRepository.save(trainingProgram);

        // Add new topics
        List<TopicTrainingProgram> newTopics = saveTrainingProgramDTO.getTopicIds().stream()
                .map(topicId -> {
                    Topic topic = topicRepository.findById(topicId)
                            .orElseThrow(() -> new RuntimeException("Topic not found"));
                    if (!topic.getStatus().equals(Status.ACTIVE)) {
                        throw new RuntimeException("Cannot add inactive topic: " + topic.getId());
                    }
                    TopicTrainingProgram topicTrainingProgram = new TopicTrainingProgram();
                    topicTrainingProgram.setTrainingProgram(savedTrainingProgram);
                    topicTrainingProgram.setTopic(topic);
                    return topicTrainingProgram;
                })
                .collect(Collectors.toList());
        topicTrainingProgramRepository.saveAll(newTopics);

        // Ensure topicTrainingPrograms is not null
        if (savedTrainingProgram.getTopicTrainingPrograms() == null) {
            savedTrainingProgram.setTopicTrainingPrograms(new ArrayList<>());
        }
        savedTrainingProgram.getTopicTrainingPrograms().addAll(newTopics);

        trainingProgramMapper.toReadTrainingProgramDTO(savedTrainingProgram);
    }

    @Transactional
    @Override
    public void updateTrainingProgram(Integer trainingProgramId, SaveTrainingProgramDTO saveTrainingProgramDTO) {
        TrainingProgram existingTrainingProgram = trainingProgramRepository.findById(trainingProgramId)
                .orElseThrow(() -> new RuntimeException("TrainingProgram not found"));

        // Remove existing topics
        List<TopicTrainingProgram> existingTopics = topicTrainingProgramRepository.findByTrainingProgramId(trainingProgramId);
        topicTrainingProgramRepository.deleteAll(existingTopics);

        // Update training program
        trainingProgramMapper.updateTrainingProgramEntityFromDTO(saveTrainingProgramDTO, existingTrainingProgram);

        // Add new topics
        List<TopicTrainingProgram> newTopics = saveTrainingProgramDTO.getTopicIds().stream()
                .map(topicId -> {
                    Topic topic = topicRepository.findById(topicId)
                            .orElseThrow(() -> new RuntimeException("Topic not found"));
                    if (!topic.getStatus().equals(Status.ACTIVE)) {
                        throw new RuntimeException("Cannot add inactive topic: " + topic.getId());
                    }
                    TopicTrainingProgram topicTrainingProgram = new TopicTrainingProgram();
                    topicTrainingProgram.setTrainingProgram(existingTrainingProgram);
                    topicTrainingProgram.setTopic(topic);
                    return topicTrainingProgram;
                })
                .collect(Collectors.toList());
        topicTrainingProgramRepository.saveAll(newTopics);

        // Ensure topicTrainingPrograms is not null
        if (existingTrainingProgram.getTopicTrainingPrograms() == null) {
            existingTrainingProgram.setTopicTrainingPrograms(new ArrayList<>());
        }
        existingTrainingProgram.getTopicTrainingPrograms().addAll(newTopics);

        trainingProgramRepository.save(existingTrainingProgram);

        trainingProgramMapper.toReadTrainingProgramDTO(existingTrainingProgram);
    }

    @Override
    public List<ListByTechnicalGroupDTO> findByTechnicalGroupId(Integer technicalGroupId) {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findByTechnicalGroupId(technicalGroupId);
        return trainingPrograms.stream()
                .map(trainingProgramMapper::toListByTechnicalGroupDTO)
                .collect(Collectors.toList());
    }
}