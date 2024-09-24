package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "technical_group")
public class TechnicalGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technical_group_id", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "detail", nullable = false, columnDefinition = "TEXT")
    private String detail;

    @JsonBackReference
    @OneToMany(mappedBy = "technicalGroup")
    private List<TrainingProgram> trainingPrograms;

    @JsonBackReference
    @OneToMany(mappedBy = "technicalGroup")
    private List<Topic> topics;
}
