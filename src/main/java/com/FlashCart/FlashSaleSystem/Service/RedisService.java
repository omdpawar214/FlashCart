package com.FlashCart.FlashSaleSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void setStock(String key, Integer value) {
        redisTemplate.opsForValue().set(key, String.valueOf(value));
    }
    public Integer getStock(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? 0 : Integer.parseInt(value);
    }

    public Long decreaseStock(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long decreaseStock(String key, int quantity) {
        return redisTemplate.opsForValue().increment(key, -quantity);
    }

    public Long increaseStock(String key, int quantity) {
        return redisTemplate.opsForValue().increment(key, quantity);
    }
}