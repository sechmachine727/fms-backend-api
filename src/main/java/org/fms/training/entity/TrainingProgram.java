package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.StatusConverter;
import org.fms.training.enums.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "training_program")
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_program_id", nullable = false)
    private Integer id;

    @Column(name = "training_program_name", nullable = false, length = 250)
    private String trainingProgramName;

    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

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

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "technical_group_id", nullable = false)
    private TechnicalGroup technicalGroup;

    @JsonBackReference
    @OneToMany(mappedBy = "trainingProgram")
    private List<Group> groups;

    @JsonBackReference
    @OneToMany(mappedBy = "trainingProgram")
    private List<TopicTrainingProgram> topicTrainingPrograms;
}
