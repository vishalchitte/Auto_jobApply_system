package com.emailjob.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_schedule")
@Data
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String hrContact;
    private String round;
    private String status;

    private LocalDateTime interviewDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
