package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "slot_time_time_section")
public class SlotTimeTimeSection {
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "slot_time_id")
    SlotTime slotTime;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "time_section_id")
    TimeSection timeSection;

    @Id
    @Column(name = "slot_time_time_section_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slotTimeTimeSectionId;
}
