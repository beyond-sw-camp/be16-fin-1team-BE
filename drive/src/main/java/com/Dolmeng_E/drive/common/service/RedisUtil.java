package com.Dolmeng_E.drive.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RedisUtil {
    private final RedisTemplate<String, Object> template;

    public Object getData(String key) {
        ValueOperations valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public void deleteData(String key) {
        template.delete(key);
    }
}
