package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Integer id;

    @Column(name = "location_name", nullable = false, length = 150)
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
}
