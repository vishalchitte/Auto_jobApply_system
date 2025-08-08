package com.emailjob.service;

import com.emailjob.entity.EmailLog;
import com.emailjob.model.EmailStatus;
import com.emailjob.repository.EmailLogRepository;
import com.emailjob.util.MailScheduler;
import com.emailjob.util.SentMail;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * EmailServiceImpl handles reading Excel, sending emails, and saving logs to
 * DB.
 */
@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailLogRepository emailLogRepo;

	@Autowired
	private SentMail sentmail;

//    private MailScheduler mailscheduler;

	@Override
	public void save(EmailLog log) {
		emailLogRepo.save(log);
	}

	/**
	 * Reads uploaded Excel file, sends emails to each HR, saves status in DB, and
	 * returns results for UI.
	 */
	@Override
	public List<EmailStatus> sendEmailsFromExcel(MultipartFile file) {
		List<EmailStatus> results = new ArrayList<>();

		try (InputStream is = file.getInputStream()) {
			Workbook workbook;

			String filename = file.getOriginalFilename();
			if (filename != null && filename.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(is); // ✅ for .xlsx
			} else if (filename != null && filename.endsWith(".xls")) {
				workbook = new HSSFWorkbook(is); // ✅ for .xls
			} else {
				throw new IllegalArgumentException("Invalid file format. Upload .xls or .xlsx");
			}

			if (workbook.getNumberOfSheets() == 0) {
				throw new IllegalStateException("Uploaded Excel file has no sheets.");
			}

			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;

				String hrName = getCellValue(row.getCell(0));
				String hrEmail = getCellValue(row.getCell(1));

				String status;
				LocalDateTime now = LocalDateTime.now();

				try {
					sentmail.Sent(hrName, hrEmail);
					status = "SENT";
				} catch (Exception e) {
					status = "FAILED";
					logger.error("Failed to send email to: {}", hrEmail, e);
				}

				EmailLog log = new EmailLog();
				log.setHrName(hrName);
				log.setHrEmail(hrEmail);
				log.setStatus(status);
				log.setSentAt(now);
				emailLogRepo.save(log);

				results.add(new EmailStatus(hrName, hrEmail, status, now));
			}

			workbook.close();

		} catch (Exception e) {
			logger.error("Error processing Excel file: ", e);
		}

		return results;
	}

	/**
	 * Utility method to get a string value from any Excel cell.
	 */
	private String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		return switch (cell.getCellType()) {
		case STRING -> cell.getStringCellValue();
		case NUMERIC -> String.valueOf(cell.getNumericCellValue());
		case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
		default -> "";
		};
	}

	/**
	 * Scheduled task to send emails from a folder every day. Runs automatically on
	 * server - no UI needed.
	 */
	// For production (daily at 9 AM): 0 0 9 * * *
	// For testing (every minute): 0 * * * * *
//    @Scheduled(cron = "0 0 9 * * *") // change this for testing as needed
//    public void sendEmailsFromScheduledFolder() {
//        logger.info("Scheduler started at {}", LocalDateTime.now());
//        mailscheduler.runScheduler();
//        logger.info("Scheduler finished");
//    }

	@Override
	public void sendEmailWithAttachments(String hrName, String hrEmail) throws Exception {
		sentmail.Sent(hrName, hrEmail); // Delegating actual send logic
		EmailLog log = new EmailLog();
		log.setHrName(hrName);
		log.setHrEmail(hrEmail);
		log.setStatus("SENT");
		log.setSentAt(LocalDateTime.now());

		emailLogRepo.save(log);

		logger.info("Mail service Run finished");
		
	}
	public void sendInstituteIdEmail(String to, String adminName, String instituteId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Institute ID Approval");
        message.setText("Hello " + adminName + ",\n\n" +
                "Your account has been approved by the Main Admin.\n" +
                "Your Institute ID is: " + instituteId + "\n\n" +
                "Please use this ID to allow your users to sign up under your account.\n\n" +
                "Regards,\nTeam");
        mailSender.send(message);
    }
}
