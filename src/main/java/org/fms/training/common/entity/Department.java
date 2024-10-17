package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id", nullable = false)
    private Integer id;

    @Column(name = "department_name", nullable = false, length = 150)
    private String departmentName;


    @JsonManagedReference
    @OneToMany(mappedBy = "department")
    private List<User> users;

    @JsonBackReference
    @OneToMany(mappedBy = "department")
    private List<TrainingProgram> trainingPrograms;
}