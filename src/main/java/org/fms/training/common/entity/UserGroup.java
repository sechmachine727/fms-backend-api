package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fms_user_group")
public class UserGroup {
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "group_id")
    Group group;

    @Id
    @Column(name = "user_group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userGroupId;
}
