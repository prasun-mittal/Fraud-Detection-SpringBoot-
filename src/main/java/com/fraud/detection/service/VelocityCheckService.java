package com.fraud.detection.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class VelocityCheckService {

    private final RedisTemplate<String, Object> redisTemplate;
    
    // Configurable thresholds
    private static final int MAX_ATTEMPTS_PER_MINUTE = 5;
    private static final int BLOCK_TIME_MINUTES = 15;

    public VelocityCheckService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isBlocked(String ipAddress) {
        String blockKey = "block:" + ipAddress;
        return Boolean.TRUE.equals(redisTemplate.hasKey(blockKey));
    }

    public int recordAttempt(String ipAddress) {
        String attemptKey = "attempt:" + ipAddress;
        
        Long attempts = redisTemplate.opsForValue().increment(attemptKey);
        
        if (attempts != null && attempts == 1) {
            // Set expiration on first attempt
            redisTemplate.expire(attemptKey, Duration.ofMinutes(1));
        }
        
        if (attempts != null && attempts > MAX_ATTEMPTS_PER_MINUTE) {
            blockIp(ipAddress);
        }
        
        return attempts != null ? attempts.intValue() : 0;
    }
    
    public void resetAttempts(String ipAddress) {
        redisTemplate.delete("attempt:" + ipAddress);
    }
    
    private void blockIp(String ipAddress) {
        String blockKey = "block:" + ipAddress;
        redisTemplate.opsForValue().set(blockKey, "blocked", Duration.ofMinutes(BLOCK_TIME_MINUTES));
    }
}
