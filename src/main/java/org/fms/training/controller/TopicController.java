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
    public ResponseEntity<List<ListTopicDTO>> findAll() {
        List<ListTopicDTO> result = topicService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailDTO> getTopicDetail(@PathVariable Integer id) {
        return topicService.getTopicDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ListTopicDTO>> searchByCodeOrName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListTopicDTO> result = topicService.searchByCodeOrName(keyword, pageable);
        return ResponseEntity.ok(result);
    }

}
