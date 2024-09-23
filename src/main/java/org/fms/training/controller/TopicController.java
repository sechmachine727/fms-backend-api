package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.Topic;
import org.fms.training.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/topics")
@RestController
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<Optional<List<Topic>>> getAll() {
        return new ResponseEntity<>(topicService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Topic>> retrieve(@PathVariable Integer id) {
        Optional<Topic> topic = topicService.findById(id);
        if (topic.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(topic);
    }
}
