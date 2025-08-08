package com.emailjob.Restcontroller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emailjob.entity.User;
import com.emailjob.repository.UserRepository;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all unapproved users
    @GetMapping("/unapproved-users")
    public List<User> getUnapprovedUsers() {
        return userRepository.findByApprovedFalse();
    }

    // ✅ Approve a user by ID
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setApproved(true);
            userRepository.save(user);
            return ResponseEntity.ok("User approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // ✅ Get all normal candidate users
    @GetMapping("/users")
    public List<User> getAllNormalUser() {
        return userRepository.findByRole("CANDIDATE");
    }

    // ✅ Delete a user by ID
    @PutMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // ✅ Admin login (supports ADMIN and MAIN_ADMIN, plain password for now)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequestUser) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequestUser.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();

        // ✅ Plain password check for now (switch to BCrypt later)
        if (!user.getPassword().equals(loginRequestUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // ✅ Role-based handling (supports ADMIN, MAIN_ADMIN, CANDIDATE)
        String role = user.getRole().toUpperCase();
        if (!role.equals("ADMIN") && !role.equals("MAIN_ADMIN") && !role.equals("CANDIDATE")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized role");
        }

        // ✅ Success — return minimal login info
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole(),
            "adminId", user.getAdmin() != null ? user.getAdmin().getId() : null
        ));
    }

}
