package com.emailjob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emailjob.entity.InterviewSchedule;
import com.emailjob.entity.User;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {

	List<InterviewSchedule> findByUser(User user);

	List<InterviewSchedule> findByUserIn(List<User> users);

	List<InterviewSchedule> findByUserAdminId(Long adminId); // fetch by admin ID
}
