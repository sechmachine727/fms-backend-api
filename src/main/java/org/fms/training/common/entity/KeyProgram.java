package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "key_program")
public class KeyProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_program_id", nullable = false)
    private Integer id;

    @Column(name = "key_program_name", nullable = false, length = 150)
    private String keyProgramName;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @JsonBackReference
    @OneToMany(mappedBy = "keyProgram")
    private List<Group> groups;
}