package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.converter.StatusConverter;
import org.fms.training.common.enums.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "pass_criteria", nullable = false, columnDefinition = "TEXT")
    private String passCriteria;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Unit> units;

    @OneToMany(mappedBy = "topic")
    private List<TopicAssessment> topicAssessments;

    @JsonBackReference
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
