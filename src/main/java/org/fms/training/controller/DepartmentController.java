package org.fms.training.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @GetMapping
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = Arrays.asList("FSA.DN", "FSA.HN", "FSA.HCM", "FSA.QN");
        return ResponseEntity.ok(departments);
    }
}
