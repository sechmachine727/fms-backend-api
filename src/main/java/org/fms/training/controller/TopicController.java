package org.fms.training.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.fms.training.common.constant.Authorization;
import org.fms.training.common.dto.topicdto.ListTopicDTO;
import org.fms.training.common.dto.topicdto.TopicDetailDTO;
import org.fms.training.common.enums.Status;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/topics")
@RestController
public class TopicController {
    private final TopicService topicService;

    @RolesAllowed({Authorization.CONTENT_MANAGER, Authorization.FA_MANAGER, Authorization.DELIVERABLES_MANAGER})
    @GetMapping
    public ResponseEntity<List<ListTopicDTO>> getAllTopics(
            @RequestParam(required = false) String search
    ) {
        return topicService.searchByCodeOrName(search)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER, Authorization.FA_MANAGER, Authorization.DELIVERABLES_MANAGER, Authorization.GROUP_ADMIN, Authorization.TRAINER})
    @GetMapping("/{id}")
    public ResponseEntity<TopicDetailDTO> getTopicDetail(@PathVariable Integer id) {
        return topicService.getTopicDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.CONTENT_MANAGER})
    @GetMapping("/active")
    public ResponseEntity<List<ListTopicDTO>> getActiveTopics() {
        List<ListTopicDTO> activeTopics = topicService.getActiveTopics();
        return ResponseEntity.ok(activeTopics);
    }
    @RolesAllowed({Authorization.FA_MANAGER, Authorization.CONTENT_MANAGER})
    @PutMapping("/change-status/{id}")
    public ResponseEntity<Map<String, String>> updateTopicStatus(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            Status newStatus = topicService.toggleTopicStatus(id);
            response.put("success", "Topic status updated successfully to " + newStatus);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("error", "Update topic status failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
