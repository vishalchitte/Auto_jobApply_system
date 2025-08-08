package com.emailjob.service;

import java.util.Map;
import java.util.Optional;

import com.emailjob.entity.User;
import com.emailjob.model.LoginRequest;
import com.emailjob.model.RegisterRequest;
import com.emailjob.repository.UserRepository;
import com.emailjob.security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Already Exists");
        }

        User user = new User();
        user.setFullName(request.getFullName()); // adapt name setter to your entity
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encode
        user.setMobile(request.getMobile());
        user.setRole(request.getRole());
        user.setProfilePic("default.jpg");
        user.setCreatedAt(request.getCreatedAt());
        user.setApproved(false);
        // You must set admin when creating user. If registration is public you may set a default admin or return error.
        // user.setAdmin(...);

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
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Password");
        }

        // Approval check
        if (!user.isApproved()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not approved by admin");
        }

        Long adminId = null;
        if (user.getAdmin() != null) adminId = user.getAdmin().getId();

        // Generate token (contains userId,email,role,adminId)
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole(), adminId);

        // Return token + minimal info
        return ResponseEntity.ok(Map.of(
            "token", token,
            "id", user.getId(),
            "name", user.getFullName(), // adapt to your getter
            "role", user.getRole(),
            "adminId", adminId
        ));
    }
}
