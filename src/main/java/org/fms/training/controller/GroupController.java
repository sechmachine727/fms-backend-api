package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.groupdto.ListGroupDTO;
import org.fms.training.dto.groupdto.ReadGroupDTO;
import org.fms.training.dto.groupdto.SaveGroupDTO;
import org.fms.training.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<ListGroupDTO>> getAllGroups(
            @RequestParam(required = false) String search
    ) {
        return groupService.getAllGroups(search)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadGroupDTO> getGroupById(@PathVariable Integer id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody SaveGroupDTO saveGroupDTO) {
        try {
            if (groupService.existsByGroupName(saveGroupDTO.getGroupName()) != null) {
                return ResponseEntity.badRequest().body("Group name already exists.");
            }

            if (groupService.existsByGroupCode(saveGroupDTO.getGroupCode()) != null) {
                return ResponseEntity.badRequest().body("Group code already exists.");
            }

            // Parse and validate dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime startDate;
            LocalDateTime endDate;

            try {
                startDate = LocalDateTime.parse(saveGroupDTO.getExpectedStartDate(), formatter);
                endDate = LocalDateTime.parse(saveGroupDTO.getExpectedEndDate(), formatter);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss.SSS.");
            }

            // Validate start date is not after end date
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body("Start date cannot be after end date.");
            }

            // Validate end date is not in the past
            if (endDate.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("End date cannot be in the past.");
            }

            groupService.createGroup(saveGroupDTO);
            return ResponseEntity.ok("Create group success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Save Group Failed!");
        }
    }
}
