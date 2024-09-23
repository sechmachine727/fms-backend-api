package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "calendar_topic", uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "topic_id"}))
public class CalendarTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_topic_id", nullable = false)
    private Integer id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "calendarTopic", cascade = CascadeType.ALL)
    private List<CalendarTopicSlotTime> calendarTopicSlotTimes;

    @OneToMany(mappedBy = "calendarTopic")
    private List<Lesson> lessons;
}
