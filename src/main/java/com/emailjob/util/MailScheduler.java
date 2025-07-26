package com.emailjob.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emailjob.entity.EmailLog;
import com.emailjob.service.EmailService;

@Component
public class MailScheduler {

	private static final Logger logger = LoggerFactory.getLogger(MailScheduler.class);

	@Autowired
	private EmailService emailService;

	// Every day at 9 AM (you can adjust as needed)
	@Scheduled(cron = "0 0 9 * * *")
	public void runScheduler() {
		logger.info("📅 Scheduler triggered at: {}", LocalDateTime.now());

		String folderPath = "email-files";
		File folder = new File(folderPath);

		if (!folder.exists() || !folder.isDirectory()) {
			logger.warn("📂 Folder '{}' not found or is not a directory", folderPath);
			return;
		}

		File[] files = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));
		if (files == null || files.length == 0) {
			logger.info("✅ No Excel files to process in '{}'", folderPath);
			return;
		}

		for (File file : files) {
			logger.info("📄 Processing file: {}", file.getName());
			try (InputStream is = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(is)) {

				Sheet sheet = workbook.getSheetAt(0);

				for (Row row : sheet) {
					if (row.getRowNum() == 0)
						continue; // Skip header

					String hrName = ExcelUtil.getCellValue(row.getCell(0));
					String hrEmail = ExcelUtil.getCellValue(row.getCell(1));

					String status;
					try {
						emailService.sendEmailWithAttachments(hrName, hrEmail);
						status = "SENT";
						logger.info("📤 Email sent to {} ({})", hrName, hrEmail);
					} catch (Exception e) {
						status = "FAILED";
						logger.error("❌ Failed to send email to {} ({}): {}", hrName, hrEmail, e.getMessage());
					}

				}

				// Rename file to processed_yyyyMMdd_HHmm.xlsx
				String newFileName = "processed_" + System.currentTimeMillis() + "_" + file.getName();
				File processedFile = new File(folderPath + File.separator + newFileName);
				if (file.renameTo(processedFile)) {
					logger.info("✅ Renamed processed file to {}", processedFile.getName());
				} else {
					logger.warn("⚠️ Failed to rename file: {}", file.getName());
				}

			} catch (Exception e) {
				logger.error("⚠️ Error processing file '{}': {}", file.getName(), e.getMessage());
			}
		}

		logger.info("📌 Scheduler run completed at: {}", LocalDateTime.now());
	}
}
