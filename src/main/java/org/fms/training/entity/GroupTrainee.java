package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.TraineeGroupStatusTypeConverter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "R_group_trainee")
public class GroupTrainee {
    @ManyToOne
    @MapsId("traineeId")
    @JoinColumn(name = "trainee_id")
    Trainee trainee;

    @ManyToOne
    @MapsId("traineeId")
    @JoinColumn(name = "group_id")
    Group group;

    @EmbeddedId
    private GroupTraineeKey id;

    @Column(name = "actual_reason", columnDefinition = "TEXT")
    private String actualReason;

    @Column(name = "training_start_date")
    private LocalDateTime trainingStartDate;

    @Column(name = "training_end_date")
    private LocalDateTime trainingEndDate;

    @Convert(converter = TraineeGroupStatusTypeConverter.class)
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
