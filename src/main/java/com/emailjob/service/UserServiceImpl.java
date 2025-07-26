package com.emailjob.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.emailjob.entity.User;
import com.emailjob.repository.UserRepository;

public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void approveUser(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setApproved(true);
			userRepository.save(user);
		}
		return;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
