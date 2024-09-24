package org.fms.training.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.fms.training.converter.ContactTypeConverter;
import org.fms.training.enums.ContactType;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fms_user")
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
    private ContactType contactType;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Trainer trainer;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<UserRole> userRoles;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<UserGroup> userGroups;


}
