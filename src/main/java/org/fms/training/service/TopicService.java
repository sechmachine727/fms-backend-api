package org.fms.training.service;

import org.fms.training.common.dto.topicdto.ListTopicDTO;
import org.fms.training.common.dto.topicdto.TopicDetailDTO;
import org.fms.training.common.enums.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Optional<List<ListTopicDTO>> searchByCodeOrName(String search);

    List<ListTopicDTO> getActiveTopics();

    Optional<TopicDetailDTO> getTopicDetail(Integer topicId);

    @Transactional
    Status toggleTopicStatus(Integer topicId);

}
