package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "assessment_name", nullable = false, length = 50)
    private String assessmentName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "weighted_number", nullable = false)
    private Integer weightedNumber;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "topic_id", referencedColumnName = "topic_id")
    private Topic topic;
}
