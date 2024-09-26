package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trainee_type")
public class TraineeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainee_type_id", nullable = false)
    private Integer id;

    @Column(name = "trainee_type_name", nullable = false, length = 150)
    private String traineeTypeName;

    @JsonBackReference
    @OneToMany(mappedBy = "traineeType")
    private List<Group> groups;
}
