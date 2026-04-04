package com.FlashCart.FlashSaleSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setStock(String key, Integer value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Integer getStock(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? 0 : (Integer) value;
    }

    public Long decreaseStock(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long increaseStock(String key, int quantity) {
        return redisTemplate.opsForValue().increment(key, quantity);
    }
}