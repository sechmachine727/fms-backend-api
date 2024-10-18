package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.trainingprogramdto.ListByTechnicalGroupDTO;
import org.fms.training.common.dto.trainingprogramdto.ListTrainingProgramDTO;
import org.fms.training.common.dto.trainingprogramdto.ReadTrainingProgramDTO;
import org.fms.training.common.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.common.entity.Topic;
import org.fms.training.common.entity.TopicTrainingProgram;
import org.fms.training.common.entity.TrainingProgram;
import org.fms.training.common.enums.Status;
import org.fms.training.common.enums.TrainingProgramStatus;
import org.fms.training.common.mapper.TrainingProgramMapper;
import org.fms.training.exception.InvalidDataException;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.repository.*;
import org.fms.training.service.TrainingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Optional<List<ListTrainingProgramDTO>> getAllTrainingPrograms() {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.getAllByOrderByLastModifiedDateDesc();
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
        trainingProgram.setStatus(TrainingProgramStatus.REVIEWING);

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

        validFieldsCheck(existingTrainingProgram, saveTrainingProgramDTO);

        List<TopicTrainingProgram> existingTopics = topicTrainingProgramRepository.findByTrainingProgramId(trainingProgramId);
        topicTrainingProgramRepository.deleteAll(existingTopics);

        trainingProgramMapper.updateTrainingProgramEntityFromDTO(saveTrainingProgramDTO, existingTrainingProgram);

        List<TopicTrainingProgram> newTopics = mapTopicsToTrainingProgram(saveTrainingProgramDTO.getTopicIds(), existingTrainingProgram);
        topicTrainingProgramRepository.saveAll(newTopics);

        if (existingTrainingProgram.getTopicTrainingPrograms() == null) {
            existingTrainingProgram.setTopicTrainingPrograms(new ArrayList<>());
        }
        existingTrainingProgram.getTopicTrainingPrograms().addAll(newTopics);

        existingTrainingProgram.setStatus(TrainingProgramStatus.REVIEWING);

        trainingProgramRepository.save(existingTrainingProgram);

        trainingProgramMapper.toReadTrainingProgramDTO(existingTrainingProgram);
    }

    private void validFieldsCheck(SaveTrainingProgramDTO saveTrainingProgramDTO) {
        Map<String, String> errors = new HashMap<>();

        if (trainingProgramRepository.existsByCode(saveTrainingProgramDTO.getCode())) {
            errors.put("trainingProgram", "TrainingProgram with code " + saveTrainingProgramDTO.getCode() + " already exists");
        }
        existsCheck(saveTrainingProgramDTO, errors);
    }

    private void validFieldsCheck(TrainingProgram trainingProgram, SaveTrainingProgramDTO saveTrainingProgramDTO) {
        Map<String, String> errors = new HashMap<>();

        if (!trainingProgram.getCode().equals(saveTrainingProgramDTO.getCode()) &&
                trainingProgramRepository.existsByCode(saveTrainingProgramDTO.getCode())) {
            errors.put("trainingProgram", "TrainingProgram with code " + saveTrainingProgramDTO.getCode() + " already exists");
        }
        existsCheck(saveTrainingProgramDTO, errors);
    }

    private void existsCheck(SaveTrainingProgramDTO saveTrainingProgramDTO, Map<String, String> errors) {
        if (!technicalGroupRepository.existsById(saveTrainingProgramDTO.getTechnicalGroupId())) {
            errors.put("technicalGroup", "Technical Group not found");
        }
        if (!departmentRepository.existsById(saveTrainingProgramDTO.getDepartmentId())) {
            errors.put("department", "Department not found");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public List<ListByTechnicalGroupDTO> findByTechnicalGroupId(Integer technicalGroupId) {
        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findByTechnicalGroupId(technicalGroupId);
        return trainingPrograms.stream()
                .map(trainingProgramMapper::toListByTechnicalGroupDTO)
                .toList();
    }

    @Transactional
    @Override
    public TrainingProgramStatus toggleTrainingProgramStatusToActiveAndInactive(Integer id) {
        return toggleStatus(id, TrainingProgramStatus.ACTIVE, TrainingProgramStatus.INACTIVE, "");
    }

    @Transactional
    @Override
    public TrainingProgramStatus toggleTrainingProgramStatusFromReviewingToDeclined(Integer id, String reason) {
        return toggleStatus(id, TrainingProgramStatus.REVIEWING, TrainingProgramStatus.DECLINED, reason);
    }

    @Transactional
    @Override
    public TrainingProgramStatus toggleTrainingProgramStatusFromReviewingOrDeclinedToActive(Integer id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training Program not found"));
        TrainingProgramStatus newStatus = (trainingProgram.getStatus() == TrainingProgramStatus.REVIEWING || trainingProgram.getStatus() == TrainingProgramStatus.DECLINED) ? TrainingProgramStatus.ACTIVE : TrainingProgramStatus.REVIEWING;
        trainingProgram.setStatus(newStatus);
        trainingProgramRepository.save(trainingProgram);
        return newStatus;
    }

    private TrainingProgramStatus toggleStatus(Integer id, TrainingProgramStatus fromStatus, TrainingProgramStatus toStatus, String reason) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training Program not found"));
        TrainingProgramStatus newStatus = trainingProgram.getStatus() == fromStatus ? toStatus : fromStatus;
        trainingProgram.setStatus(newStatus);
        trainingProgram.setNote(reason.trim());
        trainingProgramRepository.save(trainingProgram);
        return newStatus;
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