package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Getter
@Setter
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
