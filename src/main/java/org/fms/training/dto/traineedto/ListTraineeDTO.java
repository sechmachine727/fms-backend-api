package org.fms.training.dto.traineedto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Gender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ListTraineeDTO {
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate dob;
    private String University;
}
