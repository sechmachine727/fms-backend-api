package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/topics")
@RestController
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<ListTopicDTO>> getAllTopics(
            @RequestParam(required = false) String search
    ) {
        return topicService.searchByCodeOrName(search)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailDTO> getTopicDetail(@PathVariable Integer id) {
        return topicService.getTopicDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
