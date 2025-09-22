package dev.thangngo.lmssoftdreams.entities;

import dev.thangngo.lmssoftdreams.enums.UserRole;
import dev.thangngo.lmssoftdreams.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    private String avatar;
    private String fullName;
    private String phoneNumber;
    private String address;

    private LocalDate dob;

    private String gender;

    @Column(name = "status")
    private UserStatus status;
}
