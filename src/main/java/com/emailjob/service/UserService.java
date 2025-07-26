package com.emailjob.service;

import java.util.List;
import java.util.Optional;

import com.emailjob.entity.User;

public interface UserService {
    List<User> getAllUsers();
    void approveUser(Long userId);
    Optional<User> findByEmail(String email);
}
