package com.fraud.detection.service;

import com.fraud.detection.model.LoginAttempt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LoginAttemptTracker {


    public void addLog(LoginAttempt attempt) {
        }
    }

    public List<LoginAttempt> getRecentLogs() {
    }
}
