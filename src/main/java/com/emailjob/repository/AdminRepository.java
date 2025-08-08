package com.emailjob.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emailjob.entity.Admin;


public interface AdminRepository extends JpaRepository<Admin, Long> {

	Boolean existsByInstituteId(String instituteId);

	Optional<Admin> findByEmail(String email);

	Optional<Admin> findByInstituteId(String instituteId);

	 // 🔹 Add this for pending admins
    List<Admin> findByStatus(String status);
}
