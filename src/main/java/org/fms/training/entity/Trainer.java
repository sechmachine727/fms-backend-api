package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_trainer")
public class Trainer {
    @Id
    @Column(name = "trainer_id", nullable = false)
    private Integer id;

    @Column(name = "job_rank", nullable = false)
    private String jobRank;

    @Column(name = "certificate", nullable = false)
    private String certificate;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "professional_level", nullable = false)
    private String professionalLevel;

    @Column(name = "trainer_type", nullable = false)
    private String trainerType;

    @Column(name = "contribution_type", nullable = false)
    private String contributionType;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
