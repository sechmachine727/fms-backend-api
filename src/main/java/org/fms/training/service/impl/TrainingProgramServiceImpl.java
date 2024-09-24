package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.TechnicalGroup;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicTrainingProgram;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.mapper.TrainingProgramMapper;
import org.fms.training.repository.TechnicalGroupRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.TopicTrainingProgramRepository;
import org.fms.training.repository.TrainingProgramRepository;
import org.fms.training.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .map(trainingProgramMapper::toListDTO)
                .collect(Collectors.toList());
        return Optional.of(listTrainingProgramDTOs);
    }

    @Override
    public Optional<ReadTrainingProgramDTO> findById(Integer id) {
        return trainingProgramRepository.findById(id)
                .map(trainingProgramMapper::toReadDTO);
    }

    @Transactional
    @Override
    public ReadTrainingProgramDTO createTrainingProgram(SaveTrainingProgramDTO saveTrainingProgramDTO) {
        TrainingProgram trainingProgram = trainingProgramMapper.toEntity(saveTrainingProgramDTO);
        TechnicalGroup technicalGroup = technicalGroupRepository.findById(saveTrainingProgramDTO.getTechnicalGroupId())
                .orElseThrow(() -> new RuntimeException("TechnicalGroup not found"));
        trainingProgram.setTechnicalGroup(technicalGroup);
        TrainingProgram savedTrainingProgram = trainingProgramRepository.save(trainingProgram);
        return getReadTrainingProgramDTO(saveTrainingProgramDTO, savedTrainingProgram);
    }

    @Transactional
    @Override
    public ReadTrainingProgramDTO updateTrainingProgram(Integer trainingProgramId, SaveTrainingProgramDTO saveTrainingProgramDTO) {
        TrainingProgram existingTrainingProgram = trainingProgramRepository.findById(trainingProgramId)
                .orElseThrow(() -> new RuntimeException("TrainingProgram not found"));
        // Remove existing topics
        List<TopicTrainingProgram> existingTopics = topicTrainingProgramRepository.findByTrainingProgramId(trainingProgramId);
        topicTrainingProgramRepository.deleteAll(existingTopics);

        // Update training program
        trainingProgramMapper.updateEntityFromDTO(saveTrainingProgramDTO, existingTrainingProgram);
        trainingProgramRepository.save(existingTrainingProgram);

        // Add new topics
        return getReadTrainingProgramDTO(saveTrainingProgramDTO, existingTrainingProgram);
    }

    private ReadTrainingProgramDTO getReadTrainingProgramDTO(SaveTrainingProgramDTO saveTrainingProgramDTO, TrainingProgram savedTrainingProgram) {
        for (Integer topicId : saveTrainingProgramDTO.getTopicIds()) {
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));
            TopicTrainingProgram topicTrainingProgram = new TopicTrainingProgram();
            topicTrainingProgram.setTrainingProgram(savedTrainingProgram);
            topicTrainingProgram.setTopic(topic);
            topicTrainingProgramRepository.save(topicTrainingProgram);
        }
        return trainingProgramMapper.toReadDTO(savedTrainingProgram);
    }
}