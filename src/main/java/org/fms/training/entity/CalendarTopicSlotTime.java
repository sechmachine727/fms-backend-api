package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "calendar_topic_slot_time", uniqueConstraints = @UniqueConstraint(columnNames = {"slot_time_id", "calendar_topic_id"}))
public class CalendarTopicSlotTime {
    @ManyToOne
    @JoinColumn(name = "slot_time_id")
    SlotTime slotTime;
    @ManyToOne
    @JoinColumn(name = "calendar_topic_id")
    CalendarTopic calendarTopic;
    @Id
    @Column(name = "calendar_topic_slot_time_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer calendarTopicSlotTimeId;
}