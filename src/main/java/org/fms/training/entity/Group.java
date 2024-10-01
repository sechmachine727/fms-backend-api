package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.GroupStatusConverter;
import org.fms.training.enums.GroupStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fms_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Integer id;

    @Column(name = "group_name", nullable = false, unique = true, length = 100)
    private String groupName;

    @Column(name = "group_code", nullable = false, unique = true, length = 100)
    private String groupCode;

    @Column(name = "trainee_number", nullable = false)
    private Integer traineeNumber;

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

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserGroup> userGroups;

    @JsonBackReference
    @OneToMany(mappedBy = "group")
    private List<CalendarTopic> calendarTopics;
}
