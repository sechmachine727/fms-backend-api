package org.fms.training.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.fms.training.config.Authorization;
import org.fms.training.dto.groupdto.ListGroupDTO;
import org.fms.training.dto.groupdto.ReadGroupDTO;
import org.fms.training.dto.groupdto.SaveGroupDTO;
import org.fms.training.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @RolesAllowed({Authorization.FA_MANAGER})
    @GetMapping
    public ResponseEntity<List<ListGroupDTO>> getAllGroups(
            @RequestParam(required = false) String search
    ) {
        return groupService.getAllGroups(search)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.FA_MANAGER, Authorization.DELIVERABLES_MANAGER, Authorization.GROUP_ADMIN, Authorization.TRAINER})
    @GetMapping("/{id}")
    public ResponseEntity<ReadGroupDTO> getGroupById(@PathVariable Integer id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @RolesAllowed({Authorization.DELIVERABLES_MANAGER})
    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody SaveGroupDTO saveGroupDTO) {
        groupService.createGroup(saveGroupDTO);
        return ResponseEntity.ok("Create group success");
    }
}