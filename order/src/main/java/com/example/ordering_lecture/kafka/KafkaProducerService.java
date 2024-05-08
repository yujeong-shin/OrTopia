package com.example.ordering_lecture.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

// KafkaProducerService
/* 바라보고 있는 kafka broker topic 메세지 발행 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    @Value("${kafka.topic.alarm.name}")
    private String topicName;

    /* Kafka Template 을 이용해 Kafka Broker 전송 */

    private final KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessageToKafka(String message) {
        System.out.printf("Producer Message : %s%n",message);
        this.kafkaTemplate.send(topicName,message);
    }
}