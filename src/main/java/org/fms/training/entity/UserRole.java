package org.fms.training.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fms_user_role", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class UserRole {
    @Id
    @Column(name = "user_role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
}
