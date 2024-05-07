package com.example.ordering_lecture.securities.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,String> redisTemplate7;


    public void setValues(String key, String data) {
        ListOperations<String, String> listOps = redisTemplate7.opsForList();
        listOps.rightPush(key, data); // List의 오른쪽 끝에 데이터를 추가합니다.
        redisTemplate7.expire(key, Duration.ofDays(7)); // 키의 TTL을 설정합니다.
    }
}