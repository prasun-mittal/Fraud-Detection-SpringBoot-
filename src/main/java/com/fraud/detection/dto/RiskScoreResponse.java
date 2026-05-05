package com.fraud.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskScoreResponse {
    private String username;
    private int riskScore;
    private String action; // SAFE, OTP_REQUIRED, BLOCKED
    private String message;
}
