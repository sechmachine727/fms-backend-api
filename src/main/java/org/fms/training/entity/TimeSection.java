package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data

@NoArgsConstructor
@Entity
@Table(name = "time_section")
public class TimeSection {
    @Id
    @Column(name = "time_section_id")
    private Integer timeSectionId;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @OneToMany(mappedBy = "timeSection", cascade = CascadeType.ALL)
    private List<SlotTimeTimeSection> slotTimeTimeSections;


}
