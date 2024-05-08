package com.example.ordering_lecture.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("kafka")
@AllArgsConstructor
@Slf4j
public class ProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NewTopic myTopic1;
    private final NewTopic myTopic2;

    @GetMapping("/publish/mytopic1")
    public String publishSpringTopic1() {

        String message = "publish message to my_topic_1 " + UUID.randomUUID();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(myTopic1.name(), message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
        return "done";
    }

    @GetMapping("/publish/mytopic2")
    public String publish() {

        String message = "publish message to my_topic_2 " + UUID.randomUUID();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(myTopic2.name(), message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
        return "done";
    }
}