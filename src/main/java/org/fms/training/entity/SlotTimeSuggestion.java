package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "slot_time_suggestion")
public class SlotTimeSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_time_suggestion_id", nullable = false)
    private Integer id;

    @Column(name = "suggestion_name", length = 100)
    private String suggestionName;
}
