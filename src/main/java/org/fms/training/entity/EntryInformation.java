package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "entry_information")
public class EntryInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_information_id", nullable = false)
    private Integer Id;

    @Column(name = "toeic_score")
    private Integer toeic_score;

    @Column(name = "english_communication_skill", length = 50)
    private String englishCommunicationSkill;

    @Column(name = "technical_skill", length = 50)
    private String technicalSkill;

    @Column(name = "interview_score")
    private Double interviewScore;

    @Column(name = "interview_rank", length = 20)
    private String interviewRank;

    @OneToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "trainee_id")
    private Trainee trainee;
}