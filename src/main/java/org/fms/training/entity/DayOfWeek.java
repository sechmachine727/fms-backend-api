package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "day_of_week")
public class DayOfWeek {
    @Id
    @Column(name = "day_of_week_id", nullable = false)
    private Integer id;
    @Column(name = "day", length = 50, nullable = false)
    private String day;

    @OneToMany(mappedBy = "dayOfWeek")
    private List<SlotTime> slotTimes;
}
