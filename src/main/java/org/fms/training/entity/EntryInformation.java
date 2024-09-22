package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_entry_information")
public class EntryInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_information_id", nullable = false)
    private Integer Id;

    @Column(name = "toeic_score")
    private Double toeic_score;

    @Column(name = "english_communication_skill")
    private String englishCommunicationSkill;

    @Column(name = "technical_skill")
    private String technicalSkill;

    @Column(name = "interview_score")
    private Double interviewScore;

    @Column(name = "interview_rank")
    private String interviewRank;

    @OneToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "trainee_id")
    private Trainee trainee;
}