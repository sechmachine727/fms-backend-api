package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "topic_assessment")
public class TopicAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_assessment_id", nullable = false)
    private Integer id;

    @Column(name = "asssessment_name", nullable = false)
    private String assessmentName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "weighted_number", nullable = false)
    private Integer weightedNumber;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id", referencedColumnName = "topic_id")
    private Topic topic;
}
