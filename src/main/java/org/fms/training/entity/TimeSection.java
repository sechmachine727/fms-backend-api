package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "time_section")
public class TimeSection {
    @Id
    @Column(name = "time_section_id")
    private Integer timeSectionId;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @JsonBackReference
    @OneToMany(mappedBy = "timeSection", cascade = CascadeType.ALL)
    private List<SlotTimeTimeSection> slotTimeTimeSections;


}
