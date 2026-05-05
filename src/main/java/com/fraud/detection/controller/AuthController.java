package com.fraud.detection.controller;

import com.fraud.detection.dto.LoginRequest;
import com.fraud.detection.dto.RiskScoreResponse;
import com.fraud.detection.model.LoginAttempt;
import com.fraud.detection.service.GeolocationService;
import com.fraud.detection.service.LoginAttemptTracker;
import com.fraud.detection.service.NotificationService;
import com.fraud.detection.service.RiskEngineService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RiskEngineService riskEngineService;
    private final GeolocationService geolocationService;
    private final NotificationService notificationService;
    private final LoginAttemptTracker loginAttemptTracker;

    @PostMapping("/login")
    public ResponseEntity<RiskScoreResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        
        String ipAddress = (String) request.getAttribute("capturedIpAddress");
        String userAgent = (String) request.getAttribute("capturedUserAgent");
        
        // Mock IP override for testing (e.g., from a header)
        String mockIp = request.getHeader("X-Mock-IP");
        if (mockIp != null && !mockIp.isEmpty()) {
            ipAddress = mockIp;
        }

        int riskScore = riskEngineService.calculateRiskScore(ipAddress, userAgent, loginRequest.getUsername());
        String location = geolocationService.getLocation(ipAddress);
        
        String status;
        String action;
        String message;

        if (riskScore >= 71) {
            status = "BLOCKED";
            action = "BLOCKED";
            message = "Login blocked due to suspicious activity.";
        } else if (riskScore >= 31) {
            status = "OTP_CHALLENGE";
            action = "OTP_REQUIRED";
            message = "Unusual login attempt detected. Please enter OTP.";
        } else {
            status = "SUCCESS";
            action = "SAFE";
            message = "Login successful.";
        }

        // Dummy authentication check
        if (!"user".equals(loginRequest.getUsername()) || !"password".equals(loginRequest.getPassword())) {
            status = "FAILED_PASSWORD";
            action = "FAILED";
            message = "Invalid credentials.";
        }

        // Record attempt for admin dashboard
        LoginAttempt attempt = LoginAttempt.builder()
                .id(UUID.randomUUID().toString())
                .username(loginRequest.getUsername())
                .ipAddress(ipAddress)
                .location(location)
                .userAgent(userAgent)
                .riskScore(riskScore)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
                
        loginAttemptTracker.addLog(attempt);
        
        // Send notification on suspicious success or high risk
        if ("SUCCESS".equals(status) && riskScore > 10) {
           notificationService.sendSecurityAlert(loginRequest.getUsername(), ipAddress, location);
        }

        return ResponseEntity.ok(new RiskScoreResponse(loginRequest.getUsername(), riskScore, action, message));
    }
}
