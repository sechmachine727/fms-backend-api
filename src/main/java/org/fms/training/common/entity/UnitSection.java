package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "unit_section")
public class UnitSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_section_id", nullable = false)
    private Integer id;

    @Column(name = "section_number", nullable = false)
    private Integer sectionNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "delivery_type", nullable = false, length = 50)
    private String deliveryType;

    @Column(name = "duration", nullable = false)
    private Double duration;

    @Column(name = "training_format", nullable = false, length = 50)
    private String trainingFormat;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
}
