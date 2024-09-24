package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "action")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id", nullable = false)
    private Integer id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "actor", nullable = false)
    private String actor;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
