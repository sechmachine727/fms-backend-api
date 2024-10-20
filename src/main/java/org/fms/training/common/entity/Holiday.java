package org.fms.training.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.converter.StatusConverter;
import org.fms.training.common.enums.Status;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "holiday")
public class Holiday {
    @Id
    @Column(name = "holiday_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;
}
