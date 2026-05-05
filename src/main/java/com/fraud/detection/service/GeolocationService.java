package com.fraud.detection.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GeolocationService {

    private static final Map<String, String> SIMULATED_LOCATIONS = Map.of(
            "127.0.0.1", "Local Network",
            "0:0:0:0:0:0:0:1", "Local Network",
            "203.0.113.50", "Moscow, Russia",
            "198.51.100.22", "Beijing, China"
    );

    public String getLocation(String ipAddress) {
        if (ipAddress == null || ipAddress.isBlank()) {
            return "Unknown";
        }

        return SIMULATED_LOCATIONS.getOrDefault(ipAddress, "Unknown Location");
    }
}
