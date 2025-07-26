package com.emailjob.util;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class SentMail {

	private static final Logger logger = LoggerFactory.getLogger(SentMail.class);

	@Autowired
	private JavaMailSender mailSender;

	public void Sent(String hrName, String hrEmail) throws MessagingException, Exception {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

		helper.setTo(hrEmail);
		helper.setSubject("Java Developer Application - Vishal Chitte (Available for Immediate Start)");

		// Email body
		String body = String.format(
				"""
						Dear %s,

						I am excited to apply for the Java Developer position at your organization. With over 3 years of experience in backend development using Core Java, Spring Boot, and REST APIs, I have built and integrated scalable services including CIBIL score APIs and loan processing tools for clients like ICICI Bank.

						I bring strong problem-solving skills, Agile experience, and a passion for clean, efficient code. I'm also familiar with basic React.js, giving me full-stack flexibility when needed.

						I look forward to contributing to your team and discussing how I can add value to your ongoing projects.

						Total Experience: 3
						Relevant Java Experience: 3.6+
						Location: Pune, Maharashtra
						Current CTC: 7 LPA
						Expected CTC: 10 LPA
						Notice Period: Immediately Available (Negotiable)

						Warm regards,
						Vishal Chitte
						📞 +91 7038170804
						✉️ vishalchitte50@gmail.com
						📍 Pune, India
						""",
				hrName);

		helper.setText(body);

		// Attach resume PDF
		ClassPathResource attachment = new ClassPathResource("attachments/Vishal_Chitte_Java_Backend_Developer.pdf");
		if (!attachment.exists()) {
			logger.error("Attachment not found at path: {}", attachment.getPath());
			throw new FileNotFoundException("Attachment not found: " + attachment.getPath());
		}

		helper.addAttachment("Vishal_Chitte_Java_Backend_Developer.pdf", attachment);

		// Send email
		mailSender.send(message);
		logger.info("Email sent successfully to: {}", hrEmail);
	}
}
