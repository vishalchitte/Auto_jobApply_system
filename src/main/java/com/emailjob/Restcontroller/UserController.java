package com.emailjob.Restcontroller;

import com.emailjob.entity.User;
import com.emailjob.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                if (!user.isApproved()) {
                    return ResponseEntity.status(403).body("User not approved by admin.");
                }
                return ResponseEntity.ok(user);
            }
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        return userOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }
}
