package com.fraud.detection.service;

import com.fraud.detection.model.LoginAttempt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LoginAttemptTracker {

    private static final int MAX_LOGS = 50;

    private final List<LoginAttempt> attempts = new CopyOnWriteArrayList<>();

    public void addLog(LoginAttempt attempt) {
        attempts.add(attempt);

        if (attempts.size() > MAX_LOGS) {
            attempts.remove(0);
        }
    }

    public List<LoginAttempt> getRecentLogs() {
        return attempts.stream()
                .sorted(Comparator.comparing(LoginAttempt::getTimestamp).reversed())
                .toList();
    }
}
