package com.emailjob.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class RegisterRequest {

	private String fullName;
	private String email;
	private String password;
	private String mobile;
	private String role;
	
	private LocalDateTime CreatedAt;


	public String getFormattedSentAt() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return CreatedAt != null ? CreatedAt.format(formatter) : "";
	}

}
