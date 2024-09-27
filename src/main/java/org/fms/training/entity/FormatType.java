package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "format_type")
public class FormatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "format_type_id", nullable = false)
    private Integer id;

    @Column(name = "format_type_name", nullable = false, length = 150)
    private String formatTypeName;

    @JsonBackReference
    @OneToMany(mappedBy = "formatType")
    private List<Group> groups;
}