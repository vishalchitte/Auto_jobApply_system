package com.emailjob.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.emailjob.model.EmailStatus;
import com.emailjob.service.EmailService;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;

	// Load the upload form at root path ("/")
	@GetMapping("/")
	public String loadForm() {
		return "upload-form"; // maps to /WEB-INF/views/upload-form.jsp
	}

	// Also allow GET /upload to load the same form
	@GetMapping("/upload")
	public String showUploadForm() {
		return "upload-form";
	}

	// Handle file upload and process emails
	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
		// Call service to parse Excel and send emails
		List<EmailStatus> emailStatusList = emailService.sendEmailsFromExcel(file);

		// Add the status list to model for JSP table display
		model.addAttribute("emailStatuses", emailStatusList);

		return "email-status"; // maps to /WEB-INF/views/email-status.jsp
	}

	@GetMapping("/dashboard")
	public String showDashboard() {
		return "dashboard";
	}

}
