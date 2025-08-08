package com.emailjob.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data  // <-- Lombok generates getters & setters automatically
public class InterviewScheduleDTO {
    private Long id;                  // <-- required so setId() exists
    private String companyName;
    private String hrContact;
    private String round;
    private String status;
    private LocalDateTime interviewDateTime;
    private String userName;
    private String userEmail;
}
