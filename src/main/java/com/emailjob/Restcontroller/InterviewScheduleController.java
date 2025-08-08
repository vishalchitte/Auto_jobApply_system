package com.emailjob.Restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.emailjob.dto.InterviewScheduleDTO;
import com.emailjob.entity.InterviewSchedule;
import com.emailjob.entity.User;
import com.emailjob.repository.UserRepository;
import com.emailjob.service.InterviewScheduleService;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:3000")
public class InterviewScheduleController {

    private final InterviewScheduleService interviewService;
    private final UserRepository userRepo;

    public InterviewScheduleController(InterviewScheduleService interviewService, UserRepository userRepo) {
        this.interviewService = interviewService;
        this.userRepo = userRepo;
    }

    // ✅ Create interview schedule for logged-in user
    @PostMapping
    public ResponseEntity<InterviewScheduleDTO> addSchedule(Authentication authentication,
                                                             @RequestBody InterviewSchedule schedule) {
        String email = authentication.getName(); // from JWT
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        InterviewScheduleDTO saved = interviewService.saveSchedule(currentUser.getId(), schedule);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get schedules for logged-in admin
    @GetMapping
    public List<InterviewScheduleDTO> getSchedules(Authentication authentication) {
        String email = authentication.getName(); // from JWT
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        return interviewService.getSchedulesByAdmin(currentUser.getId());
    }

    // ✅ Get schedules for logged-in user
    @GetMapping("/me")
    public List<InterviewScheduleDTO> mySchedules(Authentication authentication) {
        String email = authentication.getName(); // from JWT
        User currentUser = userRepo.findByEmail(email).orElseThrow();
        return interviewService.getSchedules(currentUser.getId());
    }
}
