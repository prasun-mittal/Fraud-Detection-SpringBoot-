package com.fraud.detection.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendSecurityAlert(String username, String ipAddress, String location) {
        log.info("Security alert for user '{}' from {} ({})", username, ipAddress, location);
    }
}
