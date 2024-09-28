package org.fms.training.service;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Page<ListTopicDTO> findAll(Pageable pageable);
    Page<ListTopicDTO> searchByCodeOrName(String keyword, Pageable pageable);
    Optional<TopicDetailDTO> getTopicDetail(Integer topicId);

}
