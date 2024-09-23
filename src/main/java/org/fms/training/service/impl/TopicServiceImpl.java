package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.Topic;
import org.fms.training.repository.TopicRepository;
import org.fms.training.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public Optional<List<Topic>> findAll() {
        return Optional.of(topicRepository.findAll());
    }

    @Override
    public Optional<Topic> findById(Integer id) {
        return topicRepository.findById(id);
    }

    @Override
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public void deleteById(Integer topicId) {
        topicRepository.deleteById(topicId);
    }

}
