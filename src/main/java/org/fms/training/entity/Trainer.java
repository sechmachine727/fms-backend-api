package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    @Column(name = "trainer_id", nullable = false)
    private Integer id;

    @Column(name = "job_rank", nullable = false, length = 20)
    private String jobRank;

    @Column(name = "certificate", nullable = false)
    private String certificate;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Column(name = "professional_level", nullable = false, length = 20)
    private String professionalLevel;

    @Column(name = "trainer_type", nullable = false, length = 20)
    private String trainerType;

    @Column(name = "contribution_type", nullable = false, length = 20)
    private String contributionType;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
