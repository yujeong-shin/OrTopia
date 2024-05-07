package com.example.ordering_lecture.securities.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    @Qualifier("6")
    private final RedisTemplate<String, String> redisTemplate6;

    public RedisPublisher(RedisTemplate<String, String> redisTemplate6) {
        this.redisTemplate6 = redisTemplate6;
    }

    public void publish(ChannelTopic topic, String message) {
        redisTemplate6.convertAndSend(topic.getTopic(), message);
    }
}