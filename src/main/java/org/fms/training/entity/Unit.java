package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_unit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id", nullable = false)
    private Integer id;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @OneToMany(mappedBy = "unit")
    private List<UnitSection> unitSections;

    @OneToMany(mappedBy = "unit")
    private List<Session> sessions;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
