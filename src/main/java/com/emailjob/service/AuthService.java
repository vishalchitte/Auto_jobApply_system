package com.emailjob.service;

import org.springframework.http.ResponseEntity;

import com.emailjob.model.LoginRequest;
import com.emailjob.model.RegisterRequest;

/*
 * AuthService.java is a Service Layer Interface that defines business logic contracts for authentication — 
 * such as registering and logging in a user.

It belongs to the Service layer in the standard Spring Boot architecture:*/

public interface AuthService {

	ResponseEntity<?> registerUser(RegisterRequest request);

	ResponseEntity<?> loginUser(LoginRequest request);
}
