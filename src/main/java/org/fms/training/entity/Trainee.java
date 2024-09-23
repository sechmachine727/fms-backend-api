package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trainee")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainee_id", nullable = false)
    private Integer Id;

    @Column(name = "name")
    private String name;

    @Column(name = "dob")
    private LocalDateTime dob;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "gpa")
    private Double gpa;

    @Column(name = "phone")
    private String phone;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "language")
    private String language;

    @Column(name = "university")
    private String university;

    @Column(name = "university_graduation_date")
    private Integer universityGraduationDate;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
    private List<GroupTrainee> groupTrainees = new ArrayList<>();

    @OneToOne(mappedBy = "trainee", cascade = CascadeType.ALL)
    private EntryInformation entryInformation;
}
