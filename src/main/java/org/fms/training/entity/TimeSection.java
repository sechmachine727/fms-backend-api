package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_time_section")
public class TimeSection {
    @Id
    @Column(name = "time_section_id")
    private Integer id;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @OneToMany(mappedBy = "timeSection")
    private Set<SubSection> subSections;

    @ManyToMany
    @JoinTable(
            name = "R_SlotTime_TimeSection",
            joinColumns = @JoinColumn(name = "time_section_id"),
            inverseJoinColumns = @JoinColumn(name = "slot_time_id"))
    private List<SlotTime> slotTimes;
}
