package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private Integer unitNumber;

    @JsonBackReference
    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UnitSection> unitSections = new ArrayList<>();

    @OneToMany(mappedBy = "unit")
    private List<Lesson> lessons;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
