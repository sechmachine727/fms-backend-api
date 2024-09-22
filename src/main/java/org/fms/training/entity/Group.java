package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.GroupStatusConverter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Integer id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "delivery_type", nullable = false)
    private String deliveryType;

    @Column(name = "trainee_type", nullable = false)
    private String traineeType;

    @Column(name = "plan_revenue", nullable = false)
    private Double planRevenue;

    @Column(name = "scope", nullable = false)
    private String scope;

    @Column(name = "format_type", nullable = false)
    private String formatType;

    @Column(name = "trainee_number", nullable = false)
    private Integer traineeNumber;

    @Column(name = "key_program", nullable = false)
    private String keyProgram;

    @Column(name = "expected_start_date", nullable = false)
    private LocalDateTime expectedStartDate;

    @Column(name = "expected_end_date", nullable = false)
    private LocalDateTime expectedEndDate;

    @Column(name = "actual_start_date", nullable = false)
    private LocalDateTime actualStartDate;

    @Column(name = "actual_end_date", nullable = false)
    private LocalDateTime actualEndDate;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Convert(converter = GroupStatusConverter.class)
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupTrainee> groupTrainees;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL)
    private Calendar calendar;

    @ManyToOne
    @JoinColumn(name = "training_program_id", nullable = false)
    private TrainingProgram trainingProgram;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Action> actions;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
}
