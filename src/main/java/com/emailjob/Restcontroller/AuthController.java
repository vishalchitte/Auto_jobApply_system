package com.emailjob.Restcontroller;

import com.emailjob.entity.Admin;
import com.emailjob.entity.User;
import com.emailjob.model.LoginRequest;
import com.emailjob.repository.AdminRepository;
import com.emailjob.repository.UserRepository;
import com.emailjob.service.AuthService;

import java.time.LocalDateTime;
import java.util.Optional;

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
	private UserRepository userRepository;

	@Autowired
	private AdminRepository adminRepository; // ✅ Needed for instituteId lookups

	/**
	 * ✅ Registration endpoint: - Candidates/Sub-Admins must provide a valid
	 * instituteId (links them to an Admin) - MAIN_ADMIN or ADMIN accounts don't
	 * require an instituteId
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerJson(@RequestBody User userRequest) {
	    // Check for duplicate email
	    if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
	    }

	    // Default values
	    userRequest.setProfilePic("default.jpg");
	    userRequest.setApproved(false);
	    userRequest.setCreatedAt(LocalDateTime.now());

	    String role = userRequest.getRole().toUpperCase();

	    if ("MAIN_ADMIN".equals(role)) {
	        // Main Admin: no parent, auto-approve
	        userRequest.setAdmin(null);
	        userRequest.setApproved(true);

	        // Auto-generate institute ID if missing
	        if (userRequest.getInstituteId() == null || userRequest.getInstituteId().isBlank()) {
	            userRequest.setInstituteId("INST-" + (int) (Math.random() * 9000 + 1000));
	        }
	    } else {
	        // ADMIN or CANDIDATE: must have institute ID
	        if (userRequest.getInstituteId() == null || userRequest.getInstituteId().isBlank()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Institute ID is required");
	        }

	        // Find parent admin by institute ID
	        Optional<User> parentAdminOpt = userRepository.findByInstituteId(userRequest.getInstituteId());
	        if (parentAdminOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Institute ID");
	        }

	        // Link to parent admin
	        User parentAdmin = parentAdminOpt.get();
	        userRequest.setAdmin(parentAdmin);
	    }

	    userRepository.save(userRequest);
	    return ResponseEntity.ok("User registered successfully. Awaiting approval.");
	}



	/**
	 * ✅ Login endpoint
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		return authService.loginUser(request);
	}
}
