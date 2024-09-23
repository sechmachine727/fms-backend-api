package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "topic_training_program", uniqueConstraints = @UniqueConstraint(columnNames = {"topic_id", "training_program_id"}))
public class TopicTrainingProgram {
    @Id
    @Column(name = "topic_training_program_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicTrainingProgramId;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    Topic topic;

    @ManyToOne
    @JoinColumn(name = "training_program_id")
    TrainingProgram trainingProgram;
}
