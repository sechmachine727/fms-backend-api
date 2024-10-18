package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.converter.GroupStatusConverter;
import org.fms.training.common.enums.GroupStatus;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fms_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Integer id;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    @Column(name = "group_code", nullable = false, unique = true, length = 100)
    private String groupCode;

    @Column(name = "trainee_number", nullable = false)
    private Integer traineeNumber;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "expected_start_date", nullable = false)
    private LocalDateTime expectedStartDate;

    @Column(name = "expected_end_date", nullable = false)
    private LocalDateTime expectedEndDate;

    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDateTime actualEndDate;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "plan_revenue")
    private Double planRevenue;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "delivery_type_id", nullable = false)
    private DeliveryType deliveryType;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "trainee_type_id", nullable = false)
    private TraineeType traineeType;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "scope_id", nullable = false)
    private Scope scope;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "format_type_id", nullable = false)
    private FormatType formatType;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "key_program_id", nullable = false)
    private KeyProgram keyProgram;

    @Convert(converter = GroupStatusConverter.class)
    @Column(name = "status", nullable = false)
    private GroupStatus status;

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupTrainee> groupTrainees;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "training_program_id", nullable = false)
    private TrainingProgram trainingProgram;

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Action> actions;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "technical_group_id", nullable = false)
    private TechnicalGroup technicalGroup;

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserGroup> userGroups;

    @JsonBackReference
    @OneToMany(mappedBy = "group")
    private List<CalendarTopic> calendarTopics;
}
