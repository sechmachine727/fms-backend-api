package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "slot_time")
public class SlotTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_time_id", nullable = false)
    private Integer slotTimeId;

    @Column(name = "slot_type", nullable = false, length = 50)
    private String slotType;

    @ManyToOne
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "slotTime", cascade = CascadeType.ALL)
    private List<SlotTimeTimeSection> slotTimeTimeSections;

    @OneToMany(mappedBy = "slotTime", cascade = CascadeType.ALL)
    private List<CalendarTopicSlotTime> calendarTopicSlotTimes;
}
