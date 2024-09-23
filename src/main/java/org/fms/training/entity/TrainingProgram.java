package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;
import org.fms.training.converter.ActiveAndInactiveStatusConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_program")
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_program_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "description", nullable = false)
    private String description;

    @Convert(converter = ActiveAndInactiveStatusConverter.class)
    @Column(name = "status", nullable = false)
    private String status;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "technical_group_id", nullable = false)
    private TechnicalGroup technicalGroup;

    @OneToMany(mappedBy = "trainingProgram")
    private List<Group> groups;
}
