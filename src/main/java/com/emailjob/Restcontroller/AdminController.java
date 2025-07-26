package com.emailjob.Restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailjob.entity.User;
import com.emailjob.repository.UserRepository;
import com.emailjob.service.UserService;

@RestController
@RequestMapping("/admin")

public class AdminController {

	@Autowired
	private UserRepository userRepository;

	// API endpoint: GET /admin/unapproved-users
	@GetMapping("/unapproved-users")
	public List<User> getUnapprovedUsers() {
		return userRepository.findByApprovedFalse();
	}

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
}
