package com.emailjob.model;

/*
 * LoginRequest.java is a DTO (Data Transfer Object) used to cleanly receive and map login data from the frontend.
It's like a "mini data box" that collects request input cleanly from frontend → backend.
*/
public class LoginRequest {

	private String email;
	private String password;

	@Override
	public String toString() {
		return "LoginRequest [email=" + email + ", password=" + password + ", getEmail()=" + getEmail()
				+ ", getPassword()=" + getPassword() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
