package com.fraud.detection.controller;

import com.fraud.detection.model.LoginAttempt;
import com.fraud.detection.service.LoginAttemptTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LoginAttemptTracker loginAttemptTracker;

    @GetMapping("/logs")
    public List<LoginAttempt> getLogs() {
        return loginAttemptTracker.getRecentLogs();
    }
}
