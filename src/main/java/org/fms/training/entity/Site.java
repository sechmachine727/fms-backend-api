package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id", nullable = false)
    private Integer id;

    @Column(name = "site_name", nullable = false, length = 150)
    private String siteName;

    @JsonBackReference
    @OneToMany(mappedBy = "site")
    private List<Location> locations;

    @JsonBackReference
    @OneToMany(mappedBy = "site")
    private List<Group> groups;
}
