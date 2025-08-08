package com.emailjob.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emailjob.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByInstituteId(String instituteId);

	List<User> findByApprovedFalse();

	List<User> findByRole(String role);
}
