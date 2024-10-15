package org.fms.training.service.impl;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.dto.unitdto.UnitDTO;
import org.fms.training.dto.unitsectiondto.UnitSectionDTO;
import org.fms.training.entity.*;
import org.fms.training.enums.Status;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.mapper.TopicMapper;
import org.fms.training.repository.TopicAssessmentRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TopicServiceImplTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private TopicAssessmentRepository topicAssessmentRepository;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private TopicServiceImpl topicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchByCodeOrName_shouldReturnTopics() {
        // given
        String search = "Java";
        Topic topic = new Topic();
        ListTopicDTO listTopicDTO = new ListTopicDTO();
        List<Topic> topics = List.of(topic);

        given(topicRepository.findByTopicCodeContainingOrTopicNameContaining(search)).willReturn(topics);
        given(topicMapper.toListDTO(topic)).willReturn(listTopicDTO);

        // when
        Optional<List<ListTopicDTO>> result = topicService.searchByCodeOrName(search);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1);
        verify(topicRepository, times(1)).findByTopicCodeContainingOrTopicNameContaining(search);
    }

    @Test
    void getTopicDetail_shouldReturnTopicDetail_whenTopicExists() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setTopicCode("T001");
        topic.setTopicName("Test Topic");

        // Set TechnicalGroup for the topic
        TechnicalGroup technicalGroup = new TechnicalGroup();
        technicalGroup.setId(1);  // Set the technical group ID
        technicalGroup.setCode("TG001");
        topic.setTechnicalGroup(technicalGroup);  // Ensure this is not null

        List<Unit> units = List.of(new Unit());
        List<TopicAssessment> topicAssessments = List.of(new TopicAssessment());

        given(topicRepository.findTopicById(topicId)).willReturn(Optional.of(topic));
        given(unitRepository.findUnitsByTopicId(topicId)).willReturn(units);
        given(topicAssessmentRepository.findTopicAssessmentsByTopicId(topicId)).willReturn(topicAssessments);

        // when
        Optional<TopicDetailDTO> result = topicService.getTopicDetail(topicId);

        // then
        assertThat(result).isPresent();
        verify(topicRepository, times(1)).findTopicById(topicId);
        verify(unitRepository, times(1)).findUnitsByTopicId(topicId);
        verify(topicAssessmentRepository, times(1)).findTopicAssessmentsByTopicId(topicId);
    }


    @Test
    void getTopicDetail_shouldReturnEmpty_whenTopicNotExists() {
        // given
        Integer topicId = 1;
        given(topicRepository.findTopicById(topicId)).willReturn(Optional.empty());

        // when
        Optional<TopicDetailDTO> result = topicService.getTopicDetail(topicId);

        // then
        assertThat(result).isEmpty();
        verify(topicRepository, times(1)).findTopicById(topicId);
    }

    @Test
    void getActiveTopics_shouldReturnActiveTopics() {
        // given
        Topic activeTopic = new Topic();
        activeTopic.setStatus(Status.ACTIVE);
        List<Topic> activeTopics = List.of(activeTopic);
        ListTopicDTO listTopicDTO = new ListTopicDTO();

        given(topicRepository.findByStatus(Status.ACTIVE)).willReturn(activeTopics);
        given(topicMapper.toListDTO(activeTopic)).willReturn(listTopicDTO);

        // when
        List<ListTopicDTO> result = topicService.getActiveTopics();

        // then
        assertThat(result).hasSize(1);
        verify(topicRepository, times(1)).findByStatus(Status.ACTIVE);
    }

    @Test
    void toggleTopicStatus_shouldToggleStatusAndReturnNewStatus_whenTopicExists() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setStatus(Status.ACTIVE);

        given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));

        // when
        Status newStatus = topicService.toggleTopicStatus(topicId);

        // then
        assertThat(newStatus).isEqualTo(Status.INACTIVE);
        verify(topicRepository, times(1)).findById(topicId);
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void toggleTopicStatus_shouldThrowException_whenTopicNotFound() {
        // given
        Integer topicId = 1;
        given(topicRepository.findById(topicId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> topicService.toggleTopicStatus(topicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Topic not found");
    }

    @Test
    void toggleTopicStatus_shouldSetInactiveWhenCurrentlyActive() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setStatus(Status.ACTIVE);

        given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));

        // when
        Status newStatus = topicService.toggleTopicStatus(topicId);

        // then
        assertThat(newStatus).isEqualTo(Status.INACTIVE);
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void toggleTopicStatus_shouldSetActiveWhenCurrentlyInactive() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setStatus(Status.INACTIVE);

        given(topicRepository.findById(topicId)).willReturn(Optional.of(topic));

        // when
        Status newStatus = topicService.toggleTopicStatus(topicId);

        // then
        assertThat(newStatus).isEqualTo(Status.ACTIVE);
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void getTopicDetail_shouldReturnDetailWithNoUnitsOrAssessments() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setTopicCode("T001");
        topic.setTopicName("Test Topic");

        TechnicalGroup technicalGroup = new TechnicalGroup();
        technicalGroup.setId(1);
        technicalGroup.setCode("TG001");
        topic.setTechnicalGroup(technicalGroup);

        given(topicRepository.findTopicById(topicId)).willReturn(Optional.of(topic));
        given(unitRepository.findUnitsByTopicId(topicId)).willReturn(List.of());
        given(topicAssessmentRepository.findTopicAssessmentsByTopicId(topicId)).willReturn(List.of());

        // when
        Optional<TopicDetailDTO> result = topicService.getTopicDetail(topicId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUnits()).isEmpty();
        assertThat(result.get().getTopicAssessments()).isEmpty();
    }

    @Test
    void getTopicDetail_shouldHandleTopicWithNullTechnicalGroup() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setTopicCode("T001");
        topic.setTopicName("Test Topic");
        topic.setTechnicalGroup(null); // No technical group

        List<Unit> units = List.of();
        List<TopicAssessment> assessments = List.of();

        given(topicRepository.findTopicById(topicId)).willReturn(Optional.of(topic));
        given(unitRepository.findUnitsByTopicId(topicId)).willReturn(units);
        given(topicAssessmentRepository.findTopicAssessmentsByTopicId(topicId)).willReturn(assessments);

        // when
        Optional<TopicDetailDTO> result = topicService.getTopicDetail(topicId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTechnicalGroup()).isNull(); // Ensure technical group is null
    }

    @Test
    void getTopicDetail_shouldReturnEmpty_whenTopicNotFound() {
        // given
        Integer topicId = 999;
        given(topicRepository.findTopicById(topicId)).willReturn(Optional.empty());

        // when
        Optional<TopicDetailDTO> result = topicService.getTopicDetail(topicId);

        // then
        assertThat(result).isEmpty();
        verify(topicRepository, times(1)).findTopicById(topicId);
    }

    @Test
    void searchByCodeOrName_shouldReturnEmpty_whenNoTopicsFound() {
        // given
        String search = "NonExistentTopic";
        given(topicRepository.findByTopicCodeContainingOrTopicNameContaining(search)).willReturn(List.of());

        // when
        Optional<List<ListTopicDTO>> result = topicService.searchByCodeOrName(search);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty();
        verify(topicRepository, times(1)).findByTopicCodeContainingOrTopicNameContaining(search);
    }

    @Test
    void mapToTopicDetailDTO_shouldMapUnitsCorrectly_andCalculateTotalDurations() {
        // given
        Topic topic = new Topic();
        topic.setId(1);
        topic.setTopicCode("T001");
        topic.setTopicName("Test Topic");

        Unit unit = Mockito.mock(Unit.class);
        UnitSection section1 = new UnitSection();
        section1.setTitle("Section 1");
        section1.setDeliveryType("Class Meeting");
        section1.setDuration(2.0);
        section1.setTrainingFormat("Online");
        section1.setNote("Note 1");

        UnitSection section2 = new UnitSection();
        section2.setTitle("Section 2");
        section2.setDeliveryType("Guides/Review");
        section2.setDuration(1.5);
        section2.setTrainingFormat("Offline");
        section2.setNote("Note 2");

        UnitSection section3 = new UnitSection();
        section3.setTitle("Section 3");
        section3.setDeliveryType("Product Increment");
        section3.setDuration(3.0);
        section3.setTrainingFormat("Blended");
        section3.setNote("Note 3");

        List<UnitSection> sections = List.of(section1, section2, section3);
        when(unit.getUnitSections()).thenReturn(sections);

        List<Unit> units = List.of(unit);

        // when
        TopicServiceImpl topicService = new TopicServiceImpl(null, null, null, null);
        TopicDetailDTO result = topicService.mapToTopicDetailDTO(topic, units, List.of());

        // then
        assertThat(result.getUnits()).hasSize(1);

        UnitDTO unitDTO = result.getUnits().get(0);
        assertThat(unitDTO.getUnitSections()).hasSize(3);

        UnitSectionDTO sectionDTO1 = unitDTO.getUnitSections().get(0);
        assertThat(sectionDTO1.getTitle()).isEqualTo("Section 1");
        assertThat(sectionDTO1.getDeliveryType()).isEqualTo("Class Meeting");
        assertThat(sectionDTO1.getDuration()).isEqualTo(2.0);
        assertThat(sectionDTO1.getTrainingFormat()).isEqualTo("Online");
        assertThat(sectionDTO1.getNote()).isEqualTo("Note 1");

        UnitSectionDTO sectionDTO2 = unitDTO.getUnitSections().get(1);
        assertThat(sectionDTO2.getTitle()).isEqualTo("Section 2");
        assertThat(sectionDTO2.getDeliveryType()).isEqualTo("Guides/Review");
        assertThat(sectionDTO2.getDuration()).isEqualTo(1.5);
        assertThat(sectionDTO2.getTrainingFormat()).isEqualTo("Offline");
        assertThat(sectionDTO2.getNote()).isEqualTo("Note 2");

        UnitSectionDTO sectionDTO3 = unitDTO.getUnitSections().get(2);
        assertThat(sectionDTO3.getTitle()).isEqualTo("Section 3");
        assertThat(sectionDTO3.getDeliveryType()).isEqualTo("Product Increment");
        assertThat(sectionDTO3.getDuration()).isEqualTo(3.0);
        assertThat(sectionDTO3.getTrainingFormat()).isEqualTo("Blended");
        assertThat(sectionDTO3.getNote()).isEqualTo("Note 3");

        // Verify total durations are calculated correctly
        assertThat(unitDTO.getTotalDurationClassMeeting()).isEqualTo(2.0);
        assertThat(unitDTO.getTotalDurationGuidesReview()).isEqualTo(1.5);
        assertThat(unitDTO.getTotalDurationProductIncrement()).isEqualTo(3.0);
        assertThat(unitDTO.getTotalDuration()).isEqualTo(6.5);
    }
}
