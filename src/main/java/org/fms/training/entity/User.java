package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.converter.ContactTypeConverter;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer Id;

    @Column(name = "account", nullable = false, unique = true)
    private String account;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(name = "status")
    private boolean status;

    @Convert(converter = ContactTypeConverter.class)
    @Column(name = "contact_type", nullable = false)
    private String contactType;

    @Column(name = "department", nullable = false)
    private String department;

    @OneToOne(mappedBy = "user")
    private Trainer trainer;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "R_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
