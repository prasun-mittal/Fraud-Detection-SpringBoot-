package com.fraud.detection.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;

@Service
@RequiredArgsConstructor
public class RiskEngineService {

    private final VelocityCheckService velocityCheckService;
    private final GeolocationService geolocationService;
    private final DeviceFingerprintService deviceFingerprintService;
    private final MlFraudDetectionClient mlFraudDetectionClient;

    /**
     * Calculates a risk score from 0-100 based on metadata.
     * 0-30: Safe
     * 31-70: Warning (OTP)
     * 71-100: Critical (Block)
     */
    public int calculateRiskScore(String ipAddress, String userAgentString, String username) {
        int score = 0;

        // 1. Velocity Check (Brute Force)
        if (velocityCheckService.isBlocked(ipAddress)) {
            return 100; // Immediate block
        }

        int attempts = velocityCheckService.recordAttempt(ipAddress);
        if (attempts > 3) {
            score += 20; // High frequency, medium risk added
        }

        // 2. Geolocation Check
        String location = geolocationService.getLocation(ipAddress);
        boolean isSuspiciousLocation = geolocationService.isSuspiciousLocation(location);
        if (isSuspiciousLocation) {
            score += 30; // Suspicious country
        }

        // 3. Device Fingerprint Check (UAParser)
        Client client = deviceFingerprintService.parseUserAgent(userAgentString);
        if (deviceFingerprintService.isSuspiciousDevice(client)) {
            score += 20; // Suspicious or bot-like user agent
        }

        // 4. ML Anomaly Detection (Python Microservice)
        double anomalyScore = mlFraudDetectionClient.getAnomalyScore(attempts, isSuspiciousLocation);
        if (anomalyScore > 0.5) {
            score += (int) (anomalyScore * 50); // Add up to 50 points based on ML confidence
        }

        // Cap at 100
        return Math.min(score, 100);
    }
}

