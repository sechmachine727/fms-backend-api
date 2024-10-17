package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "time_section")
public class TimeSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_section_id")
    private Integer timeSectionId;

    @Column(name = "start_time", nullable = false, length = 10)
    private Time startTime;

    @Column(name = "end_time", nullable = false, length = 10)
    private Time endTime;

    @JsonBackReference
    @OneToMany(mappedBy = "timeSection", cascade = CascadeType.ALL)
    private List<SlotTimeTimeSection> slotTimeTimeSections;


}
