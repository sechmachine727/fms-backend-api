package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.converter.TraineeGroupStatusTypeConverter;
import org.fms.training.common.enums.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "group_trainee")
public class GroupTrainee {
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "trainee_id")
    Trainee trainee;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "group_id")
    Group group;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_trainee_id")
    private Integer groupTraineeId;

    @Column(name = "actual_reason", columnDefinition = "TEXT")
    private String actualReason;

    @Column(name = "training_start_date")
    private LocalDateTime trainingStartDate;

    @Column(name = "training_end_date")
    private LocalDateTime trainingEndDate;

    @Convert(converter = TraineeGroupStatusTypeConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
