package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id", nullable = false)
    private Integer id;

    @Column(name = "site_name", nullable = false, length = 150)
    private String siteName;

    @OneToMany(mappedBy = "site")
    private List<Location> locations;

    @OneToMany(mappedBy = "site")
    private List<Group> groups;
}
