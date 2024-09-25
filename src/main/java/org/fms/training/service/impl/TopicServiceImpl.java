package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.entity.Topic;
import org.fms.training.mapper.TopicMapper;
import org.fms.training.repository.TopicRepository;
import org.fms.training.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Override
    public Optional<List<ListTopicDTO>> findAll() {
        List<Topic> topics = topicRepository.findAll();
        System.out.println("Mapping Topic entities to DTOs...");
        List<ListTopicDTO> listTopicDTOs = topics.stream()
                .map(topicMapper::toListDTO)
                .collect(Collectors.toList());
        return Optional.of(listTopicDTOs);
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
