package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.GenderConverter;
import org.fms.training.enums.Gender;

import java.time.LocalDate;
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
    private Integer id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "gpa", nullable = false)
    private Double gpa;

    @Column(name = "phone", nullable = false, length = 11, unique = true)
    private String phone;

    @Column(name = "national_id", nullable = false, length = 12, unique = true)
    private String nationalId;

    @Column(name = "language", nullable = false, length = 20)
    private String language;

    @Column(name = "university", length = 50)
    private String university;

    @Column(name = "university_graduation_date")
    private LocalDate universityGraduationDate;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonBackReference
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
    private List<GroupTrainee> groupTrainees = new ArrayList<>();

    @OneToOne(mappedBy = "trainee", cascade = CascadeType.ALL)
    private EntryInformation entryInformation;
}
