package com.emailjob.service;

import java.util.List;

import com.emailjob.dto.InterviewScheduleDTO;
import com.emailjob.entity.InterviewSchedule;

public interface InterviewScheduleService {

    // Save interview schedule for a user
    InterviewScheduleDTO saveSchedule(Long userId, InterviewSchedule schedule);

    // Get schedules for a specific user
    List<InterviewScheduleDTO> getSchedules(Long userId);

    // Get schedules for all users under a specific admin
    List<InterviewScheduleDTO> getSchedulesByAdmin(Long adminId);
}
