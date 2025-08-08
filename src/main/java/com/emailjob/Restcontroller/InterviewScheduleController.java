package com.emailjob.Restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emailjob.dto.InterviewScheduleDTO;
import com.emailjob.entity.InterviewSchedule;
import com.emailjob.service.InterviewScheduleService;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:3000")
public class InterviewScheduleController {

    private final InterviewScheduleService interviewService;

    public InterviewScheduleController(InterviewScheduleService interviewService) {
        this.interviewService = interviewService;
    }

    // ✅ Create interview schedule for a specific user
    @PostMapping("/{userId}")
    public ResponseEntity<InterviewScheduleDTO> addSchedule(
            @PathVariable Long userId,
            @RequestBody InterviewSchedule schedule) {
        InterviewScheduleDTO saved = interviewService.saveSchedule(userId, schedule);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get schedules for admin
    @GetMapping
    public List<InterviewScheduleDTO> getSchedules(@RequestHeader("adminId") Long adminId) {
        return interviewService.getSchedulesByAdmin(adminId);
    }
}
