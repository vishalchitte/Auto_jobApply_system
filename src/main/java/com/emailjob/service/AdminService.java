package com.emailjob.service;

import java.util.List;

import com.emailjob.entity.Admin;

public interface AdminService {
	Admin createAdmin(Admin adminRequest);

	// ✅ Method to approve sub-admin and generate instituteId
	Admin approveAdmin(Long adminId);
	
	   List<Admin> getPendingAdmins();
}
