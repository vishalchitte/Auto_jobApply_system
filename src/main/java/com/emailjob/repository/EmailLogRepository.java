package com.emailjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emailjob.entity.EmailLog;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

}
