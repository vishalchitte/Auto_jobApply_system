package com.emailjob.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.emailjob.entity.User;
import com.emailjob.model.LoginRequest;
import com.emailjob.model.RegisterRequest;
import com.emailjob.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public ResponseEntity<?> registerUser(RegisterRequest request) {
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Alerdy Exite");
		}

		User user = new User();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setMobile(request.getMobile());
		user.setRole(request.getRole());
		user.setProfilePic("default.jpg"); // Optional — already default
		user.setCreatedAt(request.getCreatedAt());//
		user.setApproved(false);
		userRepo.save(user);
		return ResponseEntity.ok("Registered successfully. Awaiting approval.");
	}

	@Override
	public ResponseEntity<?> loginUser(LoginRequest request) {
	    Optional<User> userOpt = userRepo.findByEmail(request.getEmail());
	    if (userOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
	    }

	    User user = userOpt.get();

	    // Password check
	    if (!user.getPassword().equals(request.getPassword())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Password");
	    }

	    // Approval check
	    if (!user.isApproved()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not approved by admin");
	    }

	    // ✅ Safe response with adminId
	    return ResponseEntity.ok(Map.of(
	        "id", user.getId(),
	        "name", user.getFullName(),
	        "role", user.getRole(),
	        "adminId", user.getAdmin().getId() // Important for multi-client filtering
	    ));
	}



}
