package com.emailjob.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Admin")
public class Admin {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Admin's name
    @Column(nullable = false)
    private String adminName;

    // Admin's email (must be unique to avoid duplicates)
    @Column(nullable = false, unique = true)
    private String email;

    // Admin's password (should be stored hashed with BCrypt)
    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String instituteId; // Null until approved
    
    private String status = "PENDING_APPROVAL"; // PENDING_APPROVAL / APPROVED
}
