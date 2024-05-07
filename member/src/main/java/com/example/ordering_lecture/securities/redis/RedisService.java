package com.example.ordering_lecture.securities.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class RedisService {
    @Qualifier("7")
    private final RedisTemplate<String,String> redisTemplate7;

    public RedisService(RedisTemplate<String, String> redisTemplate7) {
        this.redisTemplate7 = redisTemplate7;
    }

    public void setValues(String key, String data) {
        ListOperations<String, String> listOps = redisTemplate7.opsForList();
        listOps.rightPush(key, data); // List의 오른쪽 끝에 데이터를 추가합니다.
        log.info(key+"에 "+listOps.size(key)+"개의 data가 존재합니다.");
        redisTemplate7.expire(key, Duration.ofDays(7)); // 키의 TTL을 설정합니다.
    }
}