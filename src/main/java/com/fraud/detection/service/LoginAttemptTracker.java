package com.fraud.detection.service;

import com.fraud.detection.model.LoginAttempt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LoginAttemptTracker {

    // Thread-safe list for in-memory storage of logs for the admin dashboard
    private final List<LoginAttempt> logs = new CopyOnWriteArrayList<>();

    public void addLog(LoginAttempt attempt) {
        logs.add(0, attempt); // Add to beginning (latest first)
        if (logs.size() > 100) {
            logs.remove(logs.size() - 1); // Keep only last 100
        }
    }

    public List<LoginAttempt> getRecentLogs() {
        return Collections.unmodifiableList(logs);
    }
}
