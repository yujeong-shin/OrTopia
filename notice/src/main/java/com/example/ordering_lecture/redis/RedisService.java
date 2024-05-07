package com.example.ordering_lecture.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class RedisService {

    @Qualifier("7")
    private final RedisTemplate<String,String> redisTemplate7;

    public RedisService(RedisTemplate<String, String> redisTemplate7) {
        this.redisTemplate7 = redisTemplate7;
    }


    @Transactional(readOnly = true)
    public List<String> getValues(String key) {
        ListOperations<String, String> values = redisTemplate7.opsForList();
        return values.range(key, 0, -1); // 리스트의 전체 범위를 가져옵니다.
    }
}