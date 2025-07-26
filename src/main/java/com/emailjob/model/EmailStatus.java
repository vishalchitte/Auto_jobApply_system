package com.emailjob.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmailStatus {

	private String hrName;
	private String hrEmail;
	private String status; // SENT / FAILED / INVALID
	private LocalDateTime sentAt; // ✅ NEW FIELD

	public String getFormattedSentAt() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return sentAt != null ? sentAt.format(formatter) : "";
	}
}
