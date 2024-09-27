package org.fms.training.dto.traineedto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Gender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SaveTraineeDTO {
    private String name;
    private String email;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private Double gpa;
    private String address;
    private String nationalId;
    private String language;
    private String university;
    private LocalDate universityGraduationDate;
}
