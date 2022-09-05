package com.github.borsch.messagingpractice.kafka.taxi.task1;

import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.task1.topic}")
    private final String topic;

    @SneakyThrows
    public void produce(String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, String.valueOf(message.hashCode()), message);

        SendResult<String, String> sendResult = kafkaTemplate.send(record)
            .get(5, TimeUnit.SECONDS);
        log.info("Sending message '{}' result - {}", message, sendResult);
    }

}
