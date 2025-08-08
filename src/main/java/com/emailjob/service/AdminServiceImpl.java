package com.emailjob.service;

import com.emailjob.entity.Admin;
import com.emailjob.repository.AdminRepository;
import com.emailjob.service.AdminService;
import com.emailjob.service.EmailService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.emailjob.service.EmailService; // ✅ use your EmailService interface

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private EmailService emailService;// // ✅ Will call your sendInstituteIdEmail()

	@Override
	public Admin createAdmin(Admin adminRequest) {
		// Check if email already exists
		if (adminRepository.findByEmail(adminRequest.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}

		// New admins are pending approval, no instituteId yet
		adminRequest.setStatus("PENDING_APPROVAL");
		adminRequest.setInstituteId(null);

		return adminRepository.save(adminRequest);
	}

	@Override
	public Admin approveAdmin(Long adminId) {
		// Find admin
		Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));

		// Generate unique instituteId
		String instituteId;
		do {
			instituteId = "INST-" + (1000 + (int) (Math.random() * 9000));
		} while (adminRepository.existsByInstituteId(instituteId));

		// Update admin
		admin.setInstituteId(instituteId);
		admin.setStatus("APPROVED");

		Admin savedAdmin = adminRepository.save(admin);

		// Send email with instituteId
		emailService.sendInstituteIdEmail(savedAdmin.getEmail(), savedAdmin.getAdminName(),
				savedAdmin.getInstituteId());

		return savedAdmin;
	}

	@Override
	public List<Admin> getPendingAdmins() {
		  return adminRepository.findByStatus("PENDING_APPROVAL");
	}
}
