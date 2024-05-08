package com.example.ordering_lecture.redis;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.item.dto.ItemResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final RedisTemplate<Long, String> redisTemplate2;
    private final RedisTemplate<Long, Object> redisTemplate3;
    private final RedisTemplate<String,Object> redisTemplate7;

    public void setValues(String key, ItemResponseDto dto) {
        ZSetOperations<String, String> values = redisTemplate.opsForZSet();
        ObjectMapper objectMapper = new ObjectMapper();
        long timeStamp = System.currentTimeMillis();
        try {
            String data = objectMapper.writeValueAsString(dto);
            values.add(key,data,timeStamp*-1);
        }catch (JsonProcessingException e){
            throw new OrTopiaException(ErrorCode.REDIS_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public Set<String> getValues(String key) {
        ZSetOperations<String, String> values = redisTemplate.opsForZSet();
        return values.range(key,0,4);
    }

    @Transactional(readOnly = true)
    public List<String> getValues2(Long key) {
        ListOperations<Long, String> values = redisTemplate2.opsForList();
        return values.range(key, 0, 2);
    }

    public void setItemQuantity(Long key, int quantity){
        ValueOperations<Long, Object> valueOperations = redisTemplate3.opsForValue();
        valueOperations.set(key,String.valueOf(quantity));
    }

    public void setValuesLasEvent(String key, String data) {
        ListOperations<String, Object> listOps = redisTemplate7.opsForList();
        listOps.rightPush(key, data); // List의 오른쪽 끝에 데이터를 추가합니다.
        log.info(key+"에 "+listOps.size(key)+"개의 data가 존재합니다.");
        redisTemplate7.expire(key, Duration.ofDays(7)); // 키의 TTL을 설정합니다.
    }
}