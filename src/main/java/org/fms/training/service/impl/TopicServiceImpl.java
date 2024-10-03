package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.topicassessmentdto.TopicAssessmentDTO;
import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.dto.unitdto.UnitDTO;
import org.fms.training.dto.unitsectiondto.UnitSectionDTO;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicAssessment;
import org.fms.training.entity.Unit;
import org.fms.training.enums.Status;
import org.fms.training.mapper.TopicMapper;
import org.fms.training.repository.TopicAssessmentRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.UnitRepository;
import org.fms.training.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final UnitRepository unitRepository;
    private final TopicAssessmentRepository topicAssessmentRepository;
    private final TopicMapper topicMapper;

    @Override
    public Optional<List<ListTopicDTO>> searchByCodeOrName(String search) {
        List<Topic> topics = topicRepository.findByTopicCodeContainingOrTopicNameContaining(search);
        return Optional.of(topics.stream()
                .map(topicMapper::toListDTO)
                .toList());
    }

    @Override
    public Optional<TopicDetailDTO> getTopicDetail(Integer topicId) {
        Optional<Topic> topicOptional = topicRepository.findTopicById(topicId);
        if (topicOptional.isEmpty()) {
            return Optional.empty();
        }

        Topic topic = topicOptional.get();

        List<Unit> units = unitRepository.findUnitsByTopicId(topicId);

        List<TopicAssessment> topicAssessments = topicAssessmentRepository.findTopicAssessmentsByTopicId(topicId);

        TopicDetailDTO detailDTO = mapToTopicDetailDTO(topic, units, topicAssessments);
        return Optional.of(detailDTO);
    }


    private TopicDetailDTO mapToTopicDetailDTO(Topic topic, List<Unit> units, List<TopicAssessment> topicAssessments) {
        TopicDetailDTO dto = new TopicDetailDTO();
        dto.setId(topic.getId());
        dto.setCode(topic.getTopicCode());
        dto.setName(topic.getTopicName());
        dto.setPassCriteria(topic.getPassCriteria());
        dto.setTechnicalGroupCode(topic.getTechnicalGroup().getCode());

        List<UnitDTO> unitDTOs = units.stream().map(unit -> {
            UnitDTO unitDTO = new UnitDTO();
            unitDTO.setUnitName(unit.getUnitName());
            final Double[] totalDurationClassMeeting = {0.0};
            final Double[] totalDurationGuidesReview = {0.0};
            final Double[] totalDurationProductIncrement = {0.0};
            final Double[] totalDuration = {0.0};

            List<UnitSectionDTO> unitSectionDTOs = unit.getUnitSections().stream().map(section -> {
                UnitSectionDTO sectionDTO = new UnitSectionDTO();
                sectionDTO.setTitle(section.getTitle());
                sectionDTO.setDeliveryType(section.getDeliveryType());
                sectionDTO.setDuration(section.getDuration());
                sectionDTO.setTrainingFormat(section.getTrainingFormat());
                sectionDTO.setNote(section.getNote());
                switch (section.getDeliveryType()) {
                    case "Class Meeting":
                        totalDurationClassMeeting[0] += section.getDuration();
                        break;
                    case "Guides/Review":
                        totalDurationGuidesReview[0] += section.getDuration();
                        break;
                    case "Product Increment":
                        totalDurationProductIncrement[0] += section.getDuration();
                        break;
                    default:
                        break;
                }

                totalDuration[0] += section.getDuration();
                return sectionDTO;
            }).toList();

            unitDTO.setUnitSections(unitSectionDTOs);
            unitDTO.setTotalDurationClassMeeting(totalDurationClassMeeting[0]);
            unitDTO.setTotalDurationGuidesReview(totalDurationGuidesReview[0]);
            unitDTO.setTotalDurationProductIncrement(totalDurationProductIncrement[0]);
            unitDTO.setTotalDuration(totalDuration[0]);
            return unitDTO;
        }).toList();

        dto.setUnits(unitDTOs);

        List<TopicAssessmentDTO> assessmentDTOs = topicAssessments.stream().map(assessment -> {
            TopicAssessmentDTO assessmentDTO = new TopicAssessmentDTO();
            assessmentDTO.setAssessmentName(assessment.getAssessmentName());
            assessmentDTO.setQuantity(assessment.getQuantity());
            assessmentDTO.setWeightedNumber(assessment.getWeightedNumber());
            assessmentDTO.setNote(assessment.getNote());
            return assessmentDTO;
        }).toList();

        dto.setTopicAssessments(assessmentDTOs);
        return dto;
    }

    public List<ListTopicDTO> getActiveTopics() {
        // Tìm kiếm các topic có status là "active"
        List<Topic> activeTopics = topicRepository.findByStatus(Status.ACTIVE);
        return activeTopics.stream()
                .map(topicMapper::toListDTO)
                .toList();
    }


}
