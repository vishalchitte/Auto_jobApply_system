package com.emailjob.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_logs")
@Data                       // Generates getters, setters, toString, equals, etc.
@NoArgsConstructor          // Generates default constructor
@AllArgsConstructor         // Generates parameterized constructor
public class EmailLog {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hr_name")
    private String hrName;

    @Column(name = "hr_email")
    private String hrEmail;

    @Column(name = "status")
    private String status;  // SENT / FAILED / INVALID

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

}
