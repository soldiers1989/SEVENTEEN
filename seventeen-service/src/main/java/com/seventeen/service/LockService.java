package com.seventeen.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface LockService {

     void updataLockPassWord(String apId, LocalDateTime startTime,LocalDateTime endTime);
}
