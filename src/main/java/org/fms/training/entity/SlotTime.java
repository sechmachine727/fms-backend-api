package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_slot_time")
public class SlotTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_time_id", nullable = false)
    private Integer id;

    @Column(name = "weekday", nullable = false)
    private String weekday;

    @Column(name = "slot_type", nullable = false)
    private String slotType;

    @ManyToMany(mappedBy = "slotTimes", fetch = FetchType.LAZY)
    private Set<TimeSection> timeSections;

    @OneToMany(mappedBy = "slotTime")
    private Set<CalendarTopic> calendarTopics;
}
