package com.fraud.detection.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeolocationService {

    private final Map<String, String> mockDb = new HashMap<>();

    public GeolocationService() {
        // Normal IPs
        mockDb.put("127.0.0.1", "Localhost");
        mockDb.put("0:0:0:0:0:0:0:1", "Localhost");
        mockDb.put("192.168.1.100", "US, California");

        // VPN / Suspicious IPs
        mockDb.put("203.0.113.50", "RU, Moscow"); // Mock VPN IP
        mockDb.put("198.51.100.22", "CN, Beijing");
    }

    public String getLocation(String ipAddress) {
        // In a real scenario, this would call MaxMind or ipinfo.io
        return mockDb.getOrDefault(ipAddress, "Unknown Location");
    }

    public boolean isSuspiciousLocation(String location) {
        return location.startsWith("RU") || location.startsWith("CN") || location.equals("Unknown Location");
    }
}
