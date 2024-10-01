package org.fms.training.service;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Optional<List<ListTopicDTO>> searchByCodeOrName(String search);

    Optional<TopicDetailDTO> getTopicDetail(Integer topicId);

}
