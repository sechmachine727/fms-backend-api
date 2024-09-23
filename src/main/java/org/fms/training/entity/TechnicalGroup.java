package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "technicalGroup")
    private Set<TrainingProgram> trainingPrograms;

    @OneToMany(mappedBy = "technicalGroup")
    private Set<Topic> topics;
}
