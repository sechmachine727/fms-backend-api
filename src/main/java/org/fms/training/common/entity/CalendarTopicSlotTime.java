package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "calendar_topic_slot_time")
public class CalendarTopicSlotTime {
    @ManyToOne
    @JoinColumn(name = "slot_time_id")
    SlotTime slotTime;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "calendar_topic_id")
    CalendarTopic calendarTopic;

    @Id
    @Column(name = "calendar_topic_slot_time_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer calendarTopicSlotTimeId;
}
