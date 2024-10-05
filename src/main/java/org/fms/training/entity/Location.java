package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "location_code", nullable = false, length = 150)
    private String code;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;
}
