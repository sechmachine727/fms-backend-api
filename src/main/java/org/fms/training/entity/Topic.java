package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.ActiveAndInactiveStatusConverter;
import org.fms.training.enums.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "topic")
public class Topic {
    @Id
    @Column(name = "topic_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_code", nullable = false, length = 100)
    private String topicCode;

    @Column(name = "topic_name", nullable = false, length = 250)
    private String topicName;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "pass_criteria", nullable = false)
    private String passCriteria;

    @Convert(converter = ActiveAndInactiveStatusConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @OneToMany(mappedBy = "topic")
    private List<Unit> units;

    @OneToMany(mappedBy = "topic")
    private List<TopicAssessment> topicAssessments;

    @OneToMany(mappedBy = "topic")
    private List<CalendarTopic> calendarTopics;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "technical_group_id", nullable = false)
    private TechnicalGroup technicalGroup;

    @JsonBackReference
    @OneToMany(mappedBy = "topic")
    private List<TopicTrainingProgram> topicTrainingPrograms;
}
