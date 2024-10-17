package org.fms.training.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fms_user_role")
public class UserRole {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "role_id")
    Role role;

    @Id
    @Column(name = "user_role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userRoleId;
}
