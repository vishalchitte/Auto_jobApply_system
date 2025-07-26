package com.emailjob.service;

import com.emailjob.entity.EmailLog;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import com.emailjob.repository.EmailLogRepository;
import com.emailjob.util.SentMail;

public class EmailServiceImplTest {

	@Mock
	private JavaMailSender javaMailSender;

	@Mock
	private EmailLogRepository emailLogRepository;

	@Mock
	private SentMail sentMail;

	@InjectMocks
	private EmailServiceImpl emailService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this); // ✅ Initializes all @Mock objects
	}

	@Test
	public void testSendEmailWithAttachments() throws Exception {

		String hrName = "Test HR";
		String hrEmail = "test@gmails.com";

		// When
		doNothing().when(sentMail).Sent(hrName, hrEmail);

		emailService.sendEmailWithAttachments(hrName, hrEmail);

		// Then
		verify(sentMail, times(1)).Sent(hrName, hrEmail);
		verify(emailLogRepository, times(1)).save(any(EmailLog.class));
	}
}
