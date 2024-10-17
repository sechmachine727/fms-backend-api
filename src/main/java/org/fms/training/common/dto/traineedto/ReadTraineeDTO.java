package org.fms.training.common.dto.traineedto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.Gender;

@Data
@NoArgsConstructor
public class ReadTraineeDTO {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String dob;
    private Gender gender;
    private Double gpa;
    private String address;
    private String nationalId;
    private String language;
    private String university;
    private String universityGraduationDate;
}
