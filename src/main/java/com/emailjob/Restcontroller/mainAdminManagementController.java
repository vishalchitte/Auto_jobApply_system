package com.emailjob.Restcontroller;

import com.emailjob.entity.Admin;
import com.emailjob.repository.AdminRepository;
import com.emailjob.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin-management")
public class mainAdminManagementController {

    @Autowired
    private AdminService adminService;

    // Create sub-admin (pending approval)
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody Admin adminRequest) {
        Admin savedAdmin = adminService.createAdmin(adminRequest);
        return ResponseEntity.ok(Map.of(
                "id", savedAdmin.getId(),
                "adminName", savedAdmin.getAdminName(),
                "email", savedAdmin.getEmail(),
                "status", savedAdmin.getStatus(),
                "message", "Admin created successfully. Pending approval."
        ));
    }

    // Approve sub-admin and send institute ID
    @PutMapping("/approve/{adminId}")
    public ResponseEntity<?> approveAdmin(@PathVariable Long adminId) {
        Admin approvedAdmin = adminService.approveAdmin(adminId);
        return ResponseEntity.ok(Map.of(
                "id", approvedAdmin.getId(),
                "adminName", approvedAdmin.getAdminName(),
                "email", approvedAdmin.getEmail(),
                "instituteId", approvedAdmin.getInstituteId(),
                "status", approvedAdmin.getStatus(),
                "message", "Admin approved and Institute ID sent via email."
        ));
    }
    
    @GetMapping("/pending")
    public List<Admin> getPendingAdmins() {
        return adminService.getPendingAdmins();
    }

}
