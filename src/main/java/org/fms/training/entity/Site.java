package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id", nullable = false)
    private Integer id;

    @Column(name = "site_name", nullable = false)
    private String siteName;

    @OneToMany(mappedBy = "site")
    private List<Location> locations;

    @OneToMany(mappedBy = "site")
    private List<Group> groups;
}
