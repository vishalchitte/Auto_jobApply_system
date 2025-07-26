package com.emailjob.Restcontroller;

import com.emailjob.entity.User;
import com.emailjob.model.LoginRequest;
import com.emailjob.model.RegisterRequest;
import com.emailjob.repository.UserRepository;
import com.emailjob.service.AuthService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST controller to handle authentication requests (register, login)
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend from React app
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepo;

	// ✅ Simple JSON-based registration (no file upload)
	@PostMapping("/register")
	public ResponseEntity<?> registerJson(@RequestBody RegisterRequest req) {
		// 1. Check if user already exists
		if (userRepo.findByEmail(req.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
		}

		// 2. Create a new User entity and set fields
		User user = new User();
		user.setName(req.getFullName());
		user.setEmail(req.getEmail());
		user.setMobile(req.getMobile());
		user.setPassword(req.getPassword());
		user.setRole(req.getRole()); // Usually "CANDIDATE"
		user.setProfilePic("default.jpg"); // Placeholder image
		user.setApproved(false); // Admin approval required
		user.setCreatedAt(LocalDateTime.now());

		// 3. Save to database
		userRepo.save(user);

		// 4. Return response
		return ResponseEntity.ok("Registered successfully. Awaiting admin approval.");
	}

	// ✅ Login handler
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		return authService.loginUser(request);
	}
}
