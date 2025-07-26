package com.emailjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutomatedJobApplicationEmailSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatedJobApplicationEmailSenderApplication.class, args);
	}

}
