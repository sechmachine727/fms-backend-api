package org.fms.training.service;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.entity.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Optional<List<ListTopicDTO>> findAll();

    Optional<Topic> findById(Integer id);

    Topic save(Topic topic);

    void deleteById(Integer topicId);
}
