package com.fraud.detection.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendSecurityAlert(String username, String ipAddress, String location) {
        // In a real app, use Spring Mail
        System.out.println("==================================================");
        System.out.println("SECURITY ALERT EMAIL SENT TO " + username);
        System.out.println("Suspicious login detected from:");
        System.out.println("IP: " + ipAddress);
        System.out.println("Location: " + location);
        System.out.println("==================================================");
    }
}
