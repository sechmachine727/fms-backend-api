package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "delivery_type")
public class DeliveryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_type_id", nullable = false)
    private Integer id;

    @Column(name = "delivery_type_name", nullable = false, length = 150)
    private String deliveryTypeName;

    @JsonBackReference
    @OneToMany(mappedBy = "deliveryType")
    private List<Group> groups;
}