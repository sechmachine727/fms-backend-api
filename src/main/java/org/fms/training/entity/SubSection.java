package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_sub_section")
public class SubSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "unit_section_id", nullable = false)
    private UnitSection unitSection;

    @ManyToOne
    @JoinColumn(name = "time_section_id", nullable = false)
    private TimeSection timeSection;
}
