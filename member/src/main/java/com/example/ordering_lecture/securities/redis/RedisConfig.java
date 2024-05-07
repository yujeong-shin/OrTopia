package com.example.ordering_lecture.securities.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.host}")
    private String host;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory0() {
        return createConnectionFactoryWith(0);
    }
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate0() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory0());
        return redisTemplate;
    }
    // 알람 pub/sub 채널
    @Bean
    @Qualifier("6")
    public RedisConnectionFactory redisConnectionFactory6() {
        return createConnectionFactoryWith(6);
    }
    @Bean
    @Qualifier("6")
    public RedisTemplate<String,String> redisTemplate6() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory6());
        return redisTemplate;
    }
    // 알람 캐싱 채널
    @Bean
    @Qualifier("7")
    public RedisConnectionFactory redisConnectionFactory7() {
        return createConnectionFactoryWith(7);
    }
    @Bean
    @Qualifier("7")
    public RedisTemplate<String,String> redisTemplate7() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory7());
        return redisTemplate;
    }

    public LettuceConnectionFactory createConnectionFactoryWith(int index) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(index);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

}