package com.ali.pay.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class DistributedLockUtil {
    private final RedisTemplate<String, String> redisTemplate;

    public DistributedLockUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean acquireLock(String lockKey, String requestId, long expireTimeInSeconds) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTimeInSeconds, TimeUnit.SECONDS);
        return success != null && success;
    }

    public void releaseLock(String lockKey, String requestId) {
        String storedRequestId = redisTemplate.opsForValue().get(lockKey);
        if (requestId.equals(storedRequestId)) {
            redisTemplate.delete(lockKey);
        }
    }
}

