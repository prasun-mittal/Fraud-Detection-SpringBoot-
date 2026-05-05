package com.fraud.detection.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;

@Service
public class MlFraudDetectionClient {

    private final WebClient webClient;

    public MlFraudDetectionClient(WebClient.Builder webClientBuilder) {
        // Assume Python service runs on port 8000
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    public double getAnomalyScore(int velocityCount, boolean isSuspiciousLocation) {
        try {
            // In a real app, send actual data. Here we send simple features.
            String requestBody = String.format("{\"velocity\": %d, \"suspicious_location\": %b}", velocityCount, isSuspiciousLocation);
            
            // Expected response: {"anomaly_score": 0.85} (where > 0.5 is anomalous)
            // We use block() for simplicity in this demo, but should be reactive ideally
            MlResponse response = webClient.post()
                    .uri("/predict")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(MlResponse.class)
                    .timeout(Duration.ofSeconds(2))
                    .block();
                    
            return response != null ? response.getAnomaly_score() : 0.0;
        } catch (Exception e) {
            // Fallback if ML service is down
            System.err.println("ML Service unavailable: " + e.getMessage());
            return 0.0;
        }
    }
    
    // Internal DTO
    public static class MlResponse {
        private double anomaly_score;
        public double getAnomaly_score() { return anomaly_score; }
        public void setAnomaly_score(double anomaly_score) { this.anomaly_score = anomaly_score; }
    }
}
