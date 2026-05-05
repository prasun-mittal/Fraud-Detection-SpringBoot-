package com.fraud.detection.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginAttempt {
    private String id;
    private String username;
    private String ipAddress;
    private String location;
    private String userAgent;
    private int riskScore;
    private String status; // SUCCESS, FAILED_PASSWORD, BLOCKED, OTP_CHALLENGE
    private LocalDateTime timestamp;
}
