package com.emailjob.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.emailjob.entity.EmailLog;
import com.emailjob.model.EmailStatus;

public interface EmailService {

	/*
	 * What It Does: Defines the method sendEmailsFromExcel(...)
	 * 
	 * Accepts uploaded Excel file
	 * 
	 * Returns List<EmailStatus> (for UI result table)
	 * 
	 * 
	 */
	List<EmailStatus> sendEmailsFromExcel(MultipartFile file);

	void sendEmailWithAttachments(String hrName, String hrEmail) throws Exception;

	void save(EmailLog log); // ✅ Add this

	 void sendInstituteIdEmail(String to, String adminName, String instituteId);// for instituteId sent mail after admin aproved
}
