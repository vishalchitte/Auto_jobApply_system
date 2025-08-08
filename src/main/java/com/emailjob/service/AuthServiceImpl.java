package com.emailjob.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emailjob.entity.User;
import com.emailjob.model.LoginRequest;
import com.emailjob.model.RegisterRequest;
import com.emailjob.repository.UserRepository;
import com.emailjob.security.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> loginUser(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Invalid credentials"
                ));
            }

            User user = userOpt.get();
            Long adminId = (user.getAdmin() != null) ? user.getAdmin().getId() : null;

            String token = jwtUtil.generateToken(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    adminId
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "name", (user.getName() != null ? user.getName() : user.getFullName()),
                    "role", user.getRole(),
                    "adminId", adminId
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid credentials"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Authentication error"
            ));
        }
    }

    @Override
    public ResponseEntity<?> registerUser(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email already exists"
            ));
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setName(req.getFullName());
        user.setRole(req.getRole()); // set default role if null
        user.setApproved(false); // pending approval by admin

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User registered successfully. Awaiting approval."
        ));
    }
}
