package com.FlashCart.FlashSaleSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //lau script for atomic transactions
    private static final String STOCK_LUA_SCRIPT =
            "local stock = tonumber(redis.call('GET', KEYS[1])) " +
                    "if stock >= tonumber(ARGV[1]) then " +
                    "   return redis.call('DECRBY', KEYS[1], ARGV[1]) " +
                    "else " +
                    "   return -1 " +
                    "end";
    public Long decreaseStockAtomically(String key, int quantity) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(STOCK_LUA_SCRIPT);
        script.setResultType(Long.class);

        return redisTemplate.execute(
                script,
                Collections.singletonList(key),
                String.valueOf(quantity)
        );
    }

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

    public void setWithTTL(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}