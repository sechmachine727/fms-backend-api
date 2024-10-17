package org.fms.training.service.impl;

import org.fms.training.common.dto.topicdto.ListTopicDTO;
import org.fms.training.common.dto.topicdto.TopicDetailDTO;
import org.fms.training.common.entity.TechnicalGroup;
import org.fms.training.common.entity.Topic;
import org.fms.training.common.entity.TopicAssessment;
import org.fms.training.common.entity.Unit;
import org.fms.training.common.enums.Status;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.common.mapper.TopicMapper;
import org.fms.training.repository.TopicAssessmentRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

}
