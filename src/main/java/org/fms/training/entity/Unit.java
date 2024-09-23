package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "unit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id", nullable = false)
    private Integer id;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "unit_number", nullable = false)
    private int unitNumber;
    @OneToMany(mappedBy = "unit")
    private List<UnitSection> unitSections;

    @OneToMany(mappedBy = "unit")
    private List<Lesson> lessons;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
