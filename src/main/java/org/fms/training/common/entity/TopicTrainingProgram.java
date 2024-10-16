package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "topic_training_program")
public class TopicTrainingProgram {
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "topic_id")
    Topic topic;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "training_program_id")
    TrainingProgram trainingProgram;

    @Id
    @Column(name = "topic_training_program_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicTrainingProgramId;
}
