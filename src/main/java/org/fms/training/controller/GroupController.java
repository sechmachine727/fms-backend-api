package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.groupdto.ListGroupDTO;
import org.fms.training.dto.groupdto.ReadGroupDTO;
import org.fms.training.dto.groupdto.SaveGroupDTO;
import org.fms.training.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate;
            LocalDate endDate;

            try {
                startDate = LocalDate.parse(saveGroupDTO.getExpectedStartDate(), formatter);
                endDate = LocalDate.parse(saveGroupDTO.getExpectedEndDate(), formatter);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd.");
            }

            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body("Start date cannot be after end date.");
            }

            if (endDate.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body("End date cannot be in the past.");
            }

            groupService.createGroup(saveGroupDTO);
            return ResponseEntity.ok("Create group success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Save Group Failed!");
        }
    }
}
