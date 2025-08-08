package com.emailjob.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.emailjob.dto.InterviewScheduleDTO;
import com.emailjob.entity.InterviewSchedule;
import com.emailjob.entity.User;
import com.emailjob.repository.InterviewScheduleRepository;
import com.emailjob.repository.UserRepository;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    private final InterviewScheduleRepository interviewRepo;
    private final UserRepository userRepo;

    public InterviewScheduleServiceImpl(InterviewScheduleRepository interviewRepo, UserRepository userRepo) {
        this.interviewRepo = interviewRepo;
        this.userRepo = userRepo;
    }

    @Override
    public InterviewScheduleDTO saveSchedule(Long userId, InterviewSchedule schedule) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        schedule.setUser(user);
        InterviewSchedule saved = interviewRepo.save(schedule);

        return mapToDTO(saved);
    }

    @Override
    public List<InterviewScheduleDTO> getSchedules(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return interviewRepo.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewScheduleDTO> getSchedulesByAdmin(Long adminId) {
        return interviewRepo.findByUserAdminId(adminId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper: Map Entity → DTO
    private InterviewScheduleDTO mapToDTO(InterviewSchedule schedule) {
        InterviewScheduleDTO dto = new InterviewScheduleDTO();
        dto.setId(schedule.getId());
        dto.setCompanyName(schedule.getCompanyName());
        dto.setHrContact(schedule.getHrContact());
        dto.setRound(schedule.getRound());
        dto.setStatus(schedule.getStatus());
        dto.setInterviewDateTime(schedule.getInterviewDateTime());
        dto.setUserName(schedule.getUser().getFullName());
        dto.setUserEmail(schedule.getUser().getEmail());
        return dto;
    }
}
