package com.emailjob.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Keep for compatibility if frontend sends "fullName"
    @Transient
    @JsonProperty("fullName")
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePic;

    @Column(nullable = false)
    private String role; // MAIN_ADMIN, ADMIN, CANDIDATE

    private String mobile;

    private boolean approved;

    private LocalDateTime createdAt;

    // ✅ Self-reference: admin is also a User
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true) // allow null for main admin
    private User admin;

    // ✅ Institute ID to link users under the same admin
    @Column(name = "institute_id")
    private String instituteId;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void syncFullName() {
        this.fullName = this.name;
    }
}
