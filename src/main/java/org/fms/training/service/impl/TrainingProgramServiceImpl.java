package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicTrainingProgram;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.enums.Status;
import org.fms.training.exception.DuplicateFieldException;
import org.fms.training.exception.InvalidDataException;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.mapper.TrainingProgramMapper;
import org.fms.training.repository.*;
import org.fms.training.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final TopicRepository topicRepository;
    private final TopicTrainingProgramRepository topicTrainingProgramRepository;
    private final TechnicalGroupRepository technicalGroupRepository;
    private final TrainingProgramMapper trainingProgramMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public Optional<List<ListTrainingProgramDTO>> getAllTrainingPrograms(String search) {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findByTrainingProgramNameContainingIgnoreCaseOrCodeContainingIgnoreCase(search);
        return Optional.of(trainingPrograms.stream()
                .map(trainingProgramMapper::toListTrainingProgramDTO)
                .toList());
    }

    @Override
    public Optional<ReadTrainingProgramDTO> getTrainingProgramById(Integer id) {
        return trainingProgramRepository.findById(id)
                .map(trainingProgramMapper::toReadTrainingProgramDTO);
    }

    @Transactional
    @Override
    public void createTrainingProgram(SaveTrainingProgramDTO saveTrainingProgramDTO) {
        validFieldsCheck(saveTrainingProgramDTO);

        TrainingProgram trainingProgram = trainingProgramMapper.toTrainingProgramEntity(saveTrainingProgramDTO);

        TrainingProgram savedTrainingProgram = trainingProgramRepository.save(trainingProgram);

        List<TopicTrainingProgram> newTopics = mapTopicsToTrainingProgram(saveTrainingProgramDTO.getTopicIds(), savedTrainingProgram);
        topicTrainingProgramRepository.saveAll(newTopics);

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
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram not found"));

        if (!existingTrainingProgram.getCode().equals(saveTrainingProgramDTO.getCode()) &&
                trainingProgramRepository.existsByCode(saveTrainingProgramDTO.getCode())) {
            throw new DuplicateFieldException("TrainingProgram with code " + saveTrainingProgramDTO.getCode() + " already exists");
        }
        validFieldsCheck(saveTrainingProgramDTO);

        List<TopicTrainingProgram> existingTopics = topicTrainingProgramRepository.findByTrainingProgramId(trainingProgramId);
        topicTrainingProgramRepository.deleteAll(existingTopics);

        trainingProgramMapper.updateTrainingProgramEntityFromDTO(saveTrainingProgramDTO, existingTrainingProgram);

        List<TopicTrainingProgram> newTopics = mapTopicsToTrainingProgram(saveTrainingProgramDTO.getTopicIds(), existingTrainingProgram);
        topicTrainingProgramRepository.saveAll(newTopics);

        if (existingTrainingProgram.getTopicTrainingPrograms() == null) {
            existingTrainingProgram.setTopicTrainingPrograms(new ArrayList<>());
        }
        existingTrainingProgram.getTopicTrainingPrograms().addAll(newTopics);

        trainingProgramRepository.save(existingTrainingProgram);

        trainingProgramMapper.toReadTrainingProgramDTO(existingTrainingProgram);
    }

    private void validFieldsCheck(SaveTrainingProgramDTO saveTrainingProgramDTO) {
        if (trainingProgramRepository.existsByCode(saveTrainingProgramDTO.getCode())) {
            throw new DuplicateFieldException("TrainingProgram with code " + saveTrainingProgramDTO.getCode() + " already exists");
        }
        if (!technicalGroupRepository.existsById(saveTrainingProgramDTO.getTechnicalGroupId())) {
            throw new ResourceNotFoundException("TechnicalGroup not found");
        }
        if (!departmentRepository.existsById(saveTrainingProgramDTO.getDepartmentId())) {
            throw new ResourceNotFoundException("Department not found");
        }
    }

    @Override
    public List<ListByTechnicalGroupDTO> findByTechnicalGroupId(Integer technicalGroupId) {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findByTechnicalGroupId(technicalGroupId);
        return trainingPrograms.stream()
                .map(trainingProgramMapper::toListByTechnicalGroupDTO)
                .toList();
    }

    private List<TopicTrainingProgram> mapTopicsToTrainingProgram(List<Integer> topicIds, TrainingProgram trainingProgram) {
        return topicIds.stream()
                .map(topicId -> {
                    Topic topic = topicRepository.findById(topicId)
                            .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
                    if (!topic.getStatus().equals(Status.ACTIVE)) {
                        throw new InvalidDataException("Cannot add inactive topic: " + topic.getId());
                    }
                    TopicTrainingProgram topicTrainingProgram = new TopicTrainingProgram();
                    topicTrainingProgram.setTrainingProgram(trainingProgram);
                    topicTrainingProgram.setTopic(topic);
                    return topicTrainingProgram;
                })
                .toList();
    }
}