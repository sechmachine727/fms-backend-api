package org.fms.training.service;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.entity.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Optional<List<ListTopicDTO>> searchByCodeOrName(String search);
    List<ListTopicDTO> getActiveTopics();

    Optional<TopicDetailDTO> getTopicDetail(Integer topicId);

}
