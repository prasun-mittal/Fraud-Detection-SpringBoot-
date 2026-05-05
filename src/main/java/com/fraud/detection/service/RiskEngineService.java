package com.fraud.detection.service;

import org.springframework.stereotype.Service;

@Service
public class RiskEngineService {

    /**
     * Calculates a risk score from 0-100 based on metadata.
     * 0-30: Safe
     * 31-70: Warning (OTP)
     * 71-100: Critical (Block)
     */
    public int calculateRiskScore(String ipAddress, String userAgent, String username) {
        int score = 0;

        if (ipAddress == null || ipAddress.isBlank()) {
            score += 20;
        } else if ("203.0.113.50".equals(ipAddress)) {
            score += 85;
        } else if ("198.51.100.22".equals(ipAddress)) {
            score += 55;
        } else if (!ipAddress.startsWith("127.") && !"0:0:0:0:0:0:0:1".equals(ipAddress)) {
            score += 15;
        }

        if (userAgent == null || userAgent.isBlank()) {
            score += 20;
        }

        if (username == null || username.isBlank()) {
            score += 10;
        }

        score = Math.min(score, 100);
        return score;
    }
}
