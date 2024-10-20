package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id", nullable = false)
    private Integer id;

    @Column(name = "job_rank", length = 20)
    private String jobRank;

    @Column(name = "certificate")
    private String certificate;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "professional_level", length = 20)
    private String professionalLevel;

    @Column(name = "trainer_type", length = 20)
    private String trainerType;

    @Column(name = "contribution_type", length = 20)
    private String contributionType;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainer")
    @JsonBackReference
    private List<CalendarTopic> calendarTopics;
}
