package com.emailjob.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String Name;
	private String email;
	private String mobile;
	private String password;
	private String role;
	private boolean approved = false;
	private String profilePic = "default.jpg"; // stores the filename or relative path
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private String emailId; // user's email ID used to send
	private String appPassword; // Gmail App password
	private String customBody; // Optional: custom email body
	private String attachmentPath; // Optional: file path to resume/attachment

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}// It automatically sets createdAt just before the entity is saved

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
