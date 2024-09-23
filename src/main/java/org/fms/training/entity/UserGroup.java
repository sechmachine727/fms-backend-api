package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fms_user_group", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "group_id"}))
public class UserGroup {
    @Id
    @Column(name = "user_group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userGroupId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    Group group;
}
